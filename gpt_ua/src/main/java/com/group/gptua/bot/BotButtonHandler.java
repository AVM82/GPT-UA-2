package com.group.gptua.bot;

import com.group.gptua.bot.dto.BotButtonDto;
import com.group.gptua.dto.DtoMessage;
import com.group.gptua.service.GptMessageServiceInt;
import com.group.gptua.utils.Models;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Service
@Slf4j
public class BotButtonHandler implements BotButtonHandlerInt {

  public static final String REPLY_BUTTON = "REPLY_BUTTON";
  public static final String REPLY_CANCEL_BUTTON = "REPLY_CANCEL_BUTTON";
  public static final String TEXT_REPLY_BUTTON = "повторити питання";
  public static final String TEXT_REPLY_CANCEL_BUTTON = "відміна";
  public static final String NO_FREE_TOKEN_MESSAGE = "Немає вільного місця";
  public static final String SEPARATOR = "---";

  @Qualifier("gptMessageService")
  @Autowired
  GptMessageServiceInt gptMessageService;

  /**
   * Checks response and prepare a message to send when it is need.
   *
   * @return A message to send
   */
  @Override
  public SendMessage checkResponseCondition(String chatId, DtoMessage request, String response) {
    if (response.startsWith(NO_FREE_TOKEN_MESSAGE)) {
      SendMessage sendMessage = new SendMessage();
      sendMessage.setChatId(chatId);
      sendMessage.enableHtml(true);
      sendMessage.setText(response);

      InlineKeyboardButton replyButton = getInlineKeyboardButton(
          TEXT_REPLY_BUTTON, REPLY_BUTTON, request);

      InlineKeyboardButton replyCancelButton = getInlineKeyboardButton(
          TEXT_REPLY_CANCEL_BUTTON, REPLY_CANCEL_BUTTON, request);

      log.info("Add buttons for {}", NO_FREE_TOKEN_MESSAGE);
      InlineKeyboardMarkup inlineKeyboardMarkup =
          new InlineKeyboardMarkup(List.of(List.of(replyButton, replyCancelButton)));
      sendMessage.setReplyMarkup(inlineKeyboardMarkup);

      return sendMessage;
    }
    return null;
  }


  /**
   * Checks on button click and prepare a message to send when it is need.
   *
   * @return A message to send
   */
  @Override
  public EditMessageText checkButtonClick(Update update) {

    String callbackData = update.getCallbackQuery().getData();
    long messageId = update.getCallbackQuery().getMessage().getMessageId();
    long chatId = update.getCallbackQuery().getMessage().getChatId();

    BotButtonDto buttonDto = mapFromString(callbackData);
    if (buttonDto != null) {
      Models model = buttonDto.getDtoMessage().getModel();
      String text = buttonDto.getDtoMessage().getMessage();

      if (buttonDto.getButtonName().equals(REPLY_CANCEL_BUTTON)) {
        log.info("Start to cancel reply button for messageId {}", messageId);
        return getEditMessageText(chatId, messageId, TEXT_REPLY_CANCEL_BUTTON);

      } else if (buttonDto.getButtonName().equals(REPLY_BUTTON)) {
        log.info("Start to reply button for messageId {}", messageId);

        String message = gptMessageService
            .getAnswer(String.valueOf(chatId), new DtoMessage(text, model)).getMessage();

        if (!message.startsWith(NO_FREE_TOKEN_MESSAGE)) {
          log.info("Success reply answer for messageId {}", messageId);
          String mess = message + "\n<i>"
              + "used model: " + model + "</i>";

          return getEditMessageText(chatId, messageId, mess);
        }
      }
    }
    return null;
  }

  @NotNull
  private InlineKeyboardButton getInlineKeyboardButton(String textButton,
      String buttonName, DtoMessage request) {
    InlineKeyboardButton button = new InlineKeyboardButton();
    button.setText(textButton);
    button.setCallbackData(mapToString(
        new BotButtonDto(buttonName,
            new DtoMessage(request.getMessage(), request.getModel()))));
    return button;
  }

  /**
   * Prepares a message to send.
   *
   * @return A message to send
   */
  private EditMessageText getEditMessageText(long chatId, long messageId, String mess) {
    EditMessageText sendMessage = new EditMessageText();
    sendMessage.setChatId(chatId);
    sendMessage.enableHtml(true);
    sendMessage.setText(mess);
    sendMessage.setMessageId((int) messageId);
    return sendMessage;
  }

  private String mapToString(BotButtonDto botButtonDto) {
    return botButtonDto.getButtonName()
        + SEPARATOR + botButtonDto.getDtoMessage().getModel().name()
        + SEPARATOR + botButtonDto.getDtoMessage().getMessage();
  }

  private BotButtonDto mapFromString(String botButtonString) {
    try {
      String[] fields = botButtonString.split(SEPARATOR);
      BotButtonDto botButtonDto = new BotButtonDto();
      botButtonDto.setButtonName(fields[0]);
      botButtonDto.setDtoMessage(new DtoMessage(fields[2], Models.valueOf(fields[1])));
      return botButtonDto;
    } catch (Exception e) {
      log.error("Error to map BotButtonDto from string {}", e.getMessage());
    }
    return null;
  }

}
