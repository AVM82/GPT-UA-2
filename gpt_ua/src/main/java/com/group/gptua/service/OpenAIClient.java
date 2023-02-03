package com.group.gptua.service;

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

  public String getModels() throws IOException {
    log.info(" Start getModels method ... ");
    Request request = new Request.Builder()
        .url("https://api.openai.com/v1/models")
        .header("Authorization", "Bearer " + apiKey)
        .get()
        .build();
    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new UnexpectedException("Unexpected code " + response);
      }
      return response.body().string();
    }
  }

  public String getModel(String model) throws IOException {
    log.info(" Start getModel name is {} method ... ", model);
    Request request = new Request.Builder()
        .url("https://api.openai.com/v1/models/" + model)
        .header("Authorization", "Bearer " + apiKey)
        .get()
        .build();
    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new UnexpectedException("Unexpected code " + response);
      }
      return response.body().string();
    }
  }

}



