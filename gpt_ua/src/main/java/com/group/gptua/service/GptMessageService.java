package com.group.gptua.service;

import com.group.gptua.dto.DtoMessage;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.utils.Models;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptMessageService implements GptMessageServiceInt {

  private final OpenAiInt openAiClient;

  private final UserRequestService userRequestService;

  public GptMessageService(UserRequestService userRequestService,
      @Qualifier("openAiService") OpenAiInt openAiClient) {
    this.userRequestService = userRequestService;
    this.openAiClient = openAiClient;
  }

  /**
   * Gets answer from GPT chat.
   *
   * @param dtoMessage A dtoMessage with model and request
   * @return A dtoMessage with model and response
   */
  @Override
  public DtoMessage getAnswer(String userHash, DtoMessage dtoMessage) {
    String request = dtoMessage.getMessage();
    Models model = dtoMessage.getModel();
    String response = openAiClient.getTextMessage(model, request);
    if (!userHash.isEmpty()) {
      saveRequest(model, userHash, request, response);
    }
    return new DtoMessage(response, model);
  }

  /**
   * Saves request.
   */
  private void saveRequest(Models model, String userHash, String request, String response) {
    UserRequestEntity userRequest = new UserRequestEntity();
    userRequest.setRequest(request);
    userRequest.setModel(model);
    userRequest.setUserHash(userHash);
    userRequest.setResponse(response);
    log.info("Start save request: {}", userRequest);
    try {
      userRequestService.create(userRequest);
      log.info("Success save request");
    } catch (Exception e) {
      log.error("Error {}: {}", e.getMessage(), e.getStackTrace());
    }
  }

}
