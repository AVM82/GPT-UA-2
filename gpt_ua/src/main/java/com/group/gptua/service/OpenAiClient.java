package com.group.gptua.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.gptua.dto.RequestBodyDto;
import java.io.IOException;
import java.rmi.UnexpectedException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@NoArgsConstructor
public class OpenAiClient {

  private final OkHttpClient httpClient = new OkHttpClient();
  private final MediaType json = MediaType.get("application/json; charset=utf-8");
  private static final String URI_MODELS = "https://api.openai.com/v1/models";
  private static final String URI_COMPLETIONS = "https://api.openai.com/v1/completions";
  @Value("${gpt.token}")
  private String apiKey;

  /* All API requests should include your API key in an Authorization HTTP header as follows:
   * Authorization: Bearer YOUR_API_KEY
   */

  /**
   * Lists the currently available models, and provides basic information about each one such as the
   * owner and availability.
   *
   * @return - string of response
   */
  public String getModels() {
    log.info(" Start getModels method ... ");
    Request request = createGetRequest(URI_MODELS);
    return createResponse(request);
  }

  /**
   * Retrieves a model instance, providing basic information about the model such as the owner and
   * permissioning.
   *
   * @param model - name of model instance
   * @return - string of list of models
   */
  public String getModel(String model) {
    log.info(" Start getModels method ... ");
    Request request = createGetRequest(URI_MODELS + "/" + model);
    return createResponse(request);
  }

  /**
   * Given a prompt, the model will return one or more predicted completions, and can also return
   * the probabilities of alternative tokens at each position.
   *
   * @param requestBodyDto - request body in dto
   * @return - string of model with basic information
   */
  public String getResponse(RequestBodyDto requestBodyDto) {
    log.info(" Start getResponce method with requestBodyDTO is {} ... ", requestBodyDto);
    String requestJson;
    try {
      requestJson = new ObjectMapper().writeValueAsString(requestBodyDto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    RequestBody requestBody = RequestBody.create(requestJson, json);
    Request request = createPostRequest(URI_COMPLETIONS, requestBody);
    return createResponse(request);
  }

  private Request createGetRequest(String uri) {
    return new Request.Builder()
        .url(uri)
        .header("Authorization", "Bearer " + apiKey)
        .get()
        .build();
  }

  private Request createPostRequest(String uri, RequestBody requestBody) {
    return new Request.Builder()
        .url(uri)
        .header("Authorization", "Bearer " + apiKey)
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
}



