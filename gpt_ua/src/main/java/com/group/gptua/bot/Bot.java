package com.group.gptua.bot;

import com.group.gptua.service.OpenAiInt;
import com.group.gptua.utils.Models;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

  @Qualifier("openAiService")
  @Autowired
  OpenAiInt openAi;
  private static final String START_MESS = "Hello! Select model GPT-chat, and ask your questions!";
  private static final String WRONG_COMMAND = "<b><i>Command is wrong, try again!</i></b>";

  private final WeakHashMap<String,  Models> modelsCash = new WeakHashMap<>();

  public static String chatIDLast;
  @Value("${bot.token}")
  private String botToken;
  @Value("${bot.name}")
  private String botName;

  @Override
  public String getBotUsername() {
    return botName;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      String text = update.getMessage().getText();
      String chatId = update.getMessage().getChatId().toString();
      modelsCash.computeIfAbsent(chatId, k -> Models.values()[0]);
      log.info("Bot: {}: {}", update.getMessage().getChatId(), update.getMessage().getText());
      if (text.startsWith("/")) {
        if (text.equals("/start")) {
          startCommand(chatId);
        } else {
          changeModelOrWrongCommand(chatId,text);
        }
      } else {
        userResponse(chatId,text);
      }
      chatIDLast = update.getMessage().getChatId().toString();
    }
  }

  private void userResponse(String chatId, String text) {
    Models model = modelsCash.get(chatId);
    sendTextMessage(chatId, openAi.getTextMessage(model, text) + "\n<i>"
        + "used model: " + model + "</i>");
  }

  private void startCommand(String chatId) {
    setButtons(chatId,
        Arrays.stream(Models.values()).map(m -> "/" + m.name()).toArray(String[]::new),
        START_MESS);
  }

  private void changeModelOrWrongCommand(String chatId, String text) {
    try {
      log.info("Selected \'{}\' chat-model.", Models.valueOf(text.substring(1)).getModelName());
      modelsCash.put(chatId, Models.valueOf(text.substring(1)));
    } catch (IllegalArgumentException e) {
      sendTextMessage(chatId, WRONG_COMMAND);
    }
  }

  /**
   * Method for sending text mess.
   *
   * @param chatId the id chat for response
   * @param mess   the message
   */
  public void sendTextMessage(String chatId, String mess) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.enableHtml(true);
    sendMessage.setText(mess);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      log.error("Send Message error", e);
    }
  }


  private synchronized void setButtons(String chatId, String [] names, String text) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setChatId(chatId);
    sendMessage.setText(text);

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    sendMessage.setReplyMarkup(replyKeyboardMarkup);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow keyboardRow = new KeyboardRow();
    for (String name:names) {
      keyboardRow.add(new KeyboardButton(name));
      if (keyboardRow.size() == 2) {
        keyboard.add(keyboardRow);
        keyboardRow = new KeyboardRow();
      }
    }

    replyKeyboardMarkup.setKeyboard(keyboard);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
