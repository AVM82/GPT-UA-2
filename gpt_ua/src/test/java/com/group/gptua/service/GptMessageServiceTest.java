package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.gptua.dto.DtoMessage;
import com.group.gptua.utils.Models;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GptMessageServiceTest {

  private final GptMessageService gptMessageService = mock(GptMessageService.class);
  private final String userHash = "testUserHash";
  private final String message = "test message";
  private final Models model = Models.ADA;
  private final DtoMessage dtoMessage = new DtoMessage(message, model);
  private static final Logger log = LoggerFactory.getLogger(GptMessageServiceTest.class);

  @Test
  void getAnswer() {

    log.info("start test getAnswer");
    when(gptMessageService.getAnswer(any(), any()))
        .thenAnswer(invocation -> {
          String invUserHash = invocation.getArgument(0);
          DtoMessage invDtoMessage = invocation.getArgument(1);
          assertEquals(invUserHash, userHash);
          assertEquals(invDtoMessage.getMessage(), message);
          assertEquals(invDtoMessage.getModel(), model);
          return invDtoMessage;
        });

    assertEquals(dtoMessage, gptMessageService.getAnswer(userHash, dtoMessage));
  }

  @Test
  void getAnswerVerify() {

    log.info("start test getAnswerVerify");
    gptMessageService.getAnswer(userHash, dtoMessage);
    verify(gptMessageService, times(1)).getAnswer(userHash, dtoMessage);

  }

}