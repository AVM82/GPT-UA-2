package com.group.gptua.bot;

import com.group.gptua.dto.DtoMessage;
import com.group.gptua.service.GptMessageServiceInt;
import com.group.gptua.utils.Models;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.guava.Cache;
import org.glassfish.jersey.internal.guava.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

  @Qualifier("gptMessageService")
  @Autowired
  GptMessageServiceInt gptMessageService;
  @Qualifier("botButtonHandler")
  @Autowired
  BotButtonHandlerInt botButtonHandler;
  private static final String START_MESS = "Hello! Select model GPT-chat, and ask your questions!";
  private static final String WRONG_COMMAND = "<b><i>Command is wrong, try again!</i></b>";


  @Value("${telegram.cache.minutes_after_access}")
  private int cacheMinutesAfterAccess;
  @Value("${telegram.cache.max_size}")
  private int cacheMaxSize;

  private final Cache<String, Models> cache = CacheBuilder
      .newBuilder()
      .expireAfterAccess(cacheMinutesAfterAccess, TimeUnit.MINUTES)
      .maximumSize(cacheMaxSize)
      .build();

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
      if (cache.getIfPresent(chatId) == null) {
        log.info("New user of telegram-bot");
        cache.put(chatId, Models.values()[0]);
      }
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
    } else if (update.hasCallbackQuery()) {
      EditMessageText sendMessage = botButtonHandler.checkButtonClick(update);
      if (sendMessage != null) {
        try {
          execute(sendMessage);
        } catch (TelegramApiException e) {
          log.error("Send Message with answer by button click error", e);
        }
      }
    }
  }

  private void userResponse(String chatId, String text) {
    Models model = cache.getIfPresent(chatId);
    String mess = gptMessageService.getAnswer(chatId, new DtoMessage(text, model)).getMessage();
    SendMessage sendMessage =
        botButtonHandler.checkResponseCondition(chatId, new DtoMessage(text, model), mess);
    if (sendMessage != null) {
      try {
        execute(sendMessage);
      } catch (TelegramApiException e) {
        log.error("Send Message with button error", e);
      }
      return;
    }
    sendTextMessage(chatId,
        mess + "\n<i>"
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
      cache.put(chatId, Models.valueOf(text.substring(1)));
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
