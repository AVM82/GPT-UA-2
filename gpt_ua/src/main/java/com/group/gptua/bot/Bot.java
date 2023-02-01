package com.group.gptua.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
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
      log.info("Bot: {}: {}", update.getMessage().getChatId(), update.getMessage().getText());
      sendTextMessage(update.getMessage().getChatId().toString(), update.getMessage().getText());
      chatIDLast = update.getMessage().getChatId().toString();
    }

  }

  /**
   * Method for sending text mess.
   * @param chatId the id chat for response
   * @param mess the message
   */
  public void sendTextMessage(String chatId, String mess) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.enableHtml(true);
    sendMessage.setText(mess);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      log.error("Send Message error",e);
    }
  }

}
