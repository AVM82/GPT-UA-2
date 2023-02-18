package com.group.gptua.utils;

import com.group.gptua.dto.DtoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GptTranslater implements Translater {

  @Override
  public  DtoMessage translateToEnglish(DtoMessage message) {
    String addMessageForTranslate = "Переклади на Англійську мову:{"
        + message.getMessage() + "}";
    message.setMessage(addMessageForTranslate);
    message.setModel(Models.DAVINCI);
    log.info("Message for translate {}",message);
    return message;
  }

  @Override
  public DtoMessage translateToUkraine(DtoMessage message) {
    String addMessageForTranslate = "Переклади на Українську мову:{"
        + message.getMessage() + "}";
    message.setMessage(addMessageForTranslate);
    message.setModel(Models.DAVINCI);
    log.info("Message for translate {}",message);
    return message;
  }
}
