package com.group.gptua.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.gptua.dto.ResponseDto;
import java.io.IOException;
import java.rmi.UnexpectedException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@NoArgsConstructor
public class OpenAIClient {

  private final OkHttpClient httpClient = new OkHttpClient();
  private final MediaType json = MediaType.get("application/json; charset=utf-8");
  @Value("${gpt.token}")
  private String apiKey;

  /* All API requests should include your API key in an Authorization HTTP header as follows:
   * Authorization: Bearer YOUR_API_KEY
   */

  public Response getModels() throws IOException {
    log.info(" Start getModels method ... ");
    Request request = new Request.Builder()
        .url("https://api.openai.com/v1/models")
        .header("Authorization", "Bearer " + apiKey)
        .get()
        .build();
    return getResponse(request);
  }

  public Response firstResponse(ResponseDto message) throws Exception {
//    RequestBody requestBody = RequestBody.create(JSON, "{\"prompt\":\"" + prompt + "\"}");
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValueAsString(message);
    RequestBody requestBody = RequestBody.create(mapper.writeValueAsString(message),
        json);
    log.info(" Start firstResponse method ... ");
    Request request = new Request.Builder()
        .url("https://api.openai.com/v1/completions")
        .header("Authorization", "Bearer " + apiKey)
        .post(requestBody)
        .build();
    return getResponse(request);
  }

  private Response getResponse(Request request) throws IOException {
    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new UnexpectedException("Unexpected code " + response);
      }
      return response;
    }
  }

}



