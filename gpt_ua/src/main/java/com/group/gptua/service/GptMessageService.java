package com.group.gptua.service;

import com.group.gptua.dto.DtoMessage;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.utils.Models;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptMessageService implements GptMessageServiceInt {

  private final OpenAiService openAiClient;

  private final UserRequestService userRequestService;

  public GptMessageService(OpenAiService openAiClient, UserRequestService userRequestService) {
    this.openAiClient = openAiClient;
    this.userRequestService = userRequestService;
  }

  /**
   * Gets answer from GPT chat.
   *
   * @param dtoMessage A dtoMessage with model and request
   * @return A dtoMessage with model and response
   */
  @Override
  public DtoMessage getAnswer(DtoMessage dtoMessage) {
    String request = dtoMessage.getMessage();
    Models model = dtoMessage.getModel();
    String hashUser = dtoMessage.getHashUser();
    String response = openAiClient.getTextMessage(model, request);

    saveRequest(model, hashUser, request, response);

    return new DtoMessage(response, model, hashUser);
  }

  /**
   * Saves request.
   */
  private void saveRequest(Models model, String hashUser, String request, String response) {
    UserRequestEntity userRequest = new UserRequestEntity();
    userRequest.setRequest(request);
    userRequest.setModel(model);
    userRequest.setHashUser(hashUser);
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
