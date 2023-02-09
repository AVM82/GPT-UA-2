package com.group.gptua.service;

import com.group.gptua.dto.DtoMessage;

public interface GptMessageServiceInt {

  DtoMessage getAnswer(DtoMessage dtoMessage);

}
