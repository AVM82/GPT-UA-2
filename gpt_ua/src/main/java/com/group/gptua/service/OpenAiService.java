package com.group.gptua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.gptua.dto.ApiDto;
import com.group.gptua.dto.ApiWithMoodDto;
import com.group.gptua.dto.RequestDto;
import com.group.gptua.dto.responsegpt.Choice;
import com.group.gptua.dto.responsegpt.ResponseDto;
import com.group.gptua.model.GptUri;
import com.group.gptua.model.Moods;
import com.group.gptua.model.UserSession;
import com.group.gptua.utils.Models;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * All API requests should include your API key in an Authorization HTTP header as follows:
 * Authorization: Bearer YOUR_API_KEY.
 */
@Service
@Slf4j
@NoArgsConstructor
public class OpenAiService implements OpenAiInt {

  private final OkHttpClient httpClient = new OkHttpClient();
  private final MediaType json = MediaType.get("application/json; charset=utf-8");

  @Autowired
  RequestDtoPropertiesService propertiesService;

  /**
   * Lists the currently available models, and provides basic information about each one such as the
   * owner and availability.
   *
   * @return - string of response
   */
  public String getAllModels(UserSession userSession) {
    log.info(" Start getModels method ... ");
    Request request = createGetRequest(userSession, GptUri.URI_MODELS.getUri());
    return createResponse(request);
  }

  /**
   * Retrieves a model instance, providing basic information about the model such as the owner and
   * permissioning.
   *
   * @param model - name of model instance
   * @return - string of list of models
   */
  public String getModel(UserSession userSession, String model) {
    log.info(" Start getModels method ... ");
    Request request = createGetRequest(userSession, GptUri.URI_MODEL.getUri() + model);
    return createResponse(request);
  }

  private Request createGetRequest(UserSession userSession, String uri) {
    return new Request.Builder()
        .url(uri)
        .header("Authorization", "Bearer " + userSession.getToken().getValue())
        .get()
        .build();
  }

  private Request createPostRequest(UserSession userSession, String uri, RequestBody requestBody) {
    return new Request.Builder()
        .url(uri)
        .header("Authorization", "Bearer " + userSession.getToken().getValue())
        .post(requestBody)
        .build();
  }

  private String createResponse(Request request) {
    try (
        Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new UnexpectedException("Unexpected code " + response);
      }
      return response.body().string();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * The method sends a request to the GPT Chat URI and returns a text response temperature and
   * max_tokens are set by default. temperature - What sampling temperature to use. Higher values
   * means the model will take more risks. Try 0.9 for more creative applications, and 0 (argmax
   * sampling) for ones with a well-defined answer (We generally recommend altering this or top_p
   * but not both). max_tokens - The maximum number of tokens to generate in the completion. The
   * token count of your prompt plus max_tokens cannot exceed the model's context length. Most
   * models have a context length of 2048 tokens (except for the newest models, which support
   * 4096).
   *
   * @param model    - available models to use
   * @param question - questions to AI
   * @return - answer from the GPT Chat
   */
  @Override
  public String getTextMessage(UserSession userSession, Models model, String question) {
    log.info("Start getTextMessage method with {} and {} ", model, question);
    RequestDto requestDto = createRequestDto(model, question);
    log.info("get requestDTO : {} ", requestDto.getPrompt());
    String requestJson = toStringFromDto(requestDto);
    RequestBody requestBody = RequestBody.create(requestJson, json);
    Request request = createPostRequest(userSession, GptUri.URI_COMPLETIONS.getUri(), requestBody);
    String answer = createResponse(request);
    ResponseDto responseDto = getResponse(answer);
    log.info("START GET TEXT MESSAGE !!!");
    return getFirsAnswer(responseDto);
  }

  private RequestDto createRequestDto(Models model, String question) {
    return RequestDto.builder()
        .model(model.getModelName())
        .prompt(question)
        .maxTokens(propertiesService.getMaxTokens())
        .topP(propertiesService.getTopP())
        .temperature(propertiesService.getTemperature())
        .frequencyPenalty(propertiesService.getFrequencyPenalty())
        .presencePenalty(propertiesService.getPresencePenalty())
        .stop(propertiesService.getStop())
        .build();
  }

  private String toStringFromDto(RequestDto requestBodyDto) {
    try {
      return new ObjectMapper().writeValueAsString(requestBodyDto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private ResponseDto getResponse(String answer) {
    try {
      return new ObjectMapper().readValue(answer, ResponseDto.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> getAnswers(ResponseDto responseDto) {
    List<Choice> choices = responseDto.getChoices();
    List<String> answers = new ArrayList<>();
    for (Choice choice : choices) {
      answers.add(choice.getText());
    }
    return answers;
  }

  private String getFirsAnswer(ResponseDto responseDto) {
    return getAnswers(responseDto).get(0);
  }

  /**
   * The method returns a list of available models.
   *
   * @return - a list of available models
   */
  @Override
  public List<Models> getModels() {
    return Arrays.asList(Models.values());
  }

  /**
   * The method returns a list of available moods.
   *
   * @return - a list of available models
   */
  public List<Moods> getMoods() {
    return Arrays.asList(Moods.values());
  }

  /**
   * The method returns the corresponding request object to the chat GPT corresponding
   * to the mood style selected by the user.
   *
   * @param apiWithMoodDto - a request object containing a style-mood, a previous response,
   *                         and a model
   * @return - request object
   */
  public ApiDto createMoodDto(ApiWithMoodDto apiWithMoodDto) {
    log.info("MOOD is : {}", apiWithMoodDto.getMood());
    log.info("MODEL is : {}", apiWithMoodDto.getModel());
    log.info("PROMPT is : {}", apiWithMoodDto.getMessage());
    if (apiWithMoodDto.getMood().equals(Moods.ROUGH)) {
      return setMessage(Moods.ROUGH, apiWithMoodDto);
    } else if (apiWithMoodDto.getMood().equals(Moods.BUSINESS)) {
      return setMessage(Moods.BUSINESS, apiWithMoodDto);
    } else if (apiWithMoodDto.getMood().equals(Moods.FRIENDLY)) {
      return setMessage(Moods.FRIENDLY, apiWithMoodDto);
    } else if (apiWithMoodDto.getMood().equals(Moods.CHILDREN)) {
      return setMessage(Moods.CHILDREN, apiWithMoodDto);
    } else {
      return null;
    }
  }

  /**
   * The method creates an object that contains the appropriate fields to query the GPT chat.
   *
   * @param mood           - response style
   * @param apiWithMoodDto - an object containing the previous GPT chat response, model, response
   *                       style
   * @return - response from GPT chat
   */
  private ApiDto setMessage(Moods mood, ApiWithMoodDto apiWithMoodDto) {
    String question = createMoodQuestion(mood.getMoodName(),
        apiWithMoodDto.getMessage());
    log.info("Question is : {}", question);
    return ApiDto.builder().model(apiWithMoodDto.getModel()).prompt(question).build();
  }

  /**
   * The method creates a string to change the response style.
   *
   * @param moodName - response style
   * @param message  - previous reply from GPT chat
   * @return - modified request to GPT
   */
  private String createMoodQuestion(String moodName, String message) {
    return "Paraphrase this text in a " + moodName + " style : " + message;
  }
}



