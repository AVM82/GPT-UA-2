package com.group.gptua.bot;

import com.group.gptua.dto.DtoMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotButtonHandlerInt {

  SendMessage checkResponseCondition(String chatId, DtoMessage request, String response);

  EditMessageText checkButtonClick(Update update);


}
