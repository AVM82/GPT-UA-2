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
import java.util.concurrent.TimeUnit;
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

  @Autowired
  RequestDtoPropertiesService propertiesService;
  private final OkHttpClient httpClient = new OkHttpClient.Builder()
      .readTimeout(60, TimeUnit.SECONDS)
      .build();

  private final MediaType json = MediaType.get("application/json; charset=utf-8");

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

  protected Request createPostRequest(UserSession userSession, String uri,
      RequestBody requestBody) {
    return new Request.Builder()
        .url(uri)
        .header("Authorization", "Bearer " + userSession.getToken().getValue())
        .post(requestBody)
        .build();
  }

  protected String createResponse(Request request) {
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
    String requestJson = toStringFromDto(requestDto);
    RequestBody requestBody = RequestBody.create(requestJson, json);
    Request request = createPostRequest(userSession, GptUri.URI_COMPLETIONS.getUri(), requestBody);
    log.info("request is : {}", request);
    String answer = createResponse(request);
    ResponseDto responseDto = getResponse(answer);
    return changeAnswerFormat(getFirstAnswer(responseDto),
        propertiesService.getNumberOfWordsInLine());
  }

  protected RequestDto createRequestDto(Models model, String question) {
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

  private String getFirstAnswer(ResponseDto responseDto) {
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
   * The method creates an object that contains the appropriate fields to query the GPT chat.
   *
   * @param apiWithMoodDto - an object containing the previous GPT chat response, model, response
   *                       style
   * @return - response from GPT chat
   */
  public ApiDto setMessage(ApiWithMoodDto apiWithMoodDto) {
    String question = createMoodQuestion(apiWithMoodDto.getMood().getMoodName(),
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

  /**
   * The method changes the format of the string by the number of parts separated by spaces.
   *
   * @param answer     - response from GPT
   * @param numOfParts - number of parts followed by "\n"
   * @return - string with "\n" after the specified number of numOfParts
   */
  private String changeAnswerFormat(String answer, int numOfParts) {
    String[] parts = answer.split(" ");
    String result = "";
    int count = 0;
    for (String part : parts) {
      if (count <= numOfParts) {
        result += part + " ";
        count++;
      } else {
        result += "\n";
        count = 0;
      }
    }
    return result;
  }
}



