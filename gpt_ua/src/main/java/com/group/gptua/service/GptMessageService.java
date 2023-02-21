package com.group.gptua.service;

import com.group.gptua.dto.DtoMessage;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.utils.Models;
import com.group.gptua.utils.NoFreeTokenException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptMessageService implements GptMessageServiceInt {

  private final OpenAiInt openAiClient;

  private final UserRequestService userRequestService;
  private final UserSessionServiceInt userSessionService;
  public static final String EXCEPTION_MESSAGE = "Спробуйте пізніше, або виберіть іншу модель";
  private final MeterRegistry meterRegistry;
  private final Map<Models, Counter> modelRequestCounters = new EnumMap<>(Models.class);

  /**
   * Constructor.
   */
  public GptMessageService(UserRequestService userRequestService,
      @Qualifier("openAiService") OpenAiInt openAiClient,
      @Qualifier("userSessionService") UserSessionServiceInt userSessionService,
      MeterRegistry meterRegistry) {
    this.userRequestService = userRequestService;
    this.openAiClient = openAiClient;
    this.userSessionService = userSessionService;
    this.meterRegistry = meterRegistry;
  }

  /**
   * Post construct init method.
   */
  @PostConstruct
  public void init() {
    Arrays.asList(Models.values()).forEach(m ->
        modelRequestCounters.put(m,
            Counter.builder("request_by_model_" + m.name())
                .description("number of request by model " + m.name())
                .register(meterRegistry)));
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
    String response;
    try {
      response = openAiClient
          .getTextMessage(userSessionService.getUserSession(userHash), model, request);
    } catch (NoFreeTokenException e) {
      log.info("no free token: {}", e.getMessage());
      return new DtoMessage(e.getMessage(), model);
    } catch (Exception e) {
      log.warn("exception on getAnswer(): {} {}", e.getMessage(), e.getStackTrace());
      return new DtoMessage(EXCEPTION_MESSAGE, model);
    }
    modelRequestCounters.get(model).increment();
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
