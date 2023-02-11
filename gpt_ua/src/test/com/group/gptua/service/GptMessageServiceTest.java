package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.group.gptua.dto.DtoMessage;
import com.group.gptua.utils.Models;
import org.junit.jupiter.api.Test;

class GptMessageServiceTest {

  private final GptMessageService gptMessageService = mock(GptMessageService.class);

  @Test
  void getAnswer() {
    String userHash = "testUserHash";
    String message = "test message";
    Models model = Models.ADA;
    DtoMessage dtoMessage = new DtoMessage(message, model);

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
}