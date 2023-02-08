package com.group.gptua.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptTokenService {

  @Value("${gpt.token}")
  private String apiKey;
  private final HashSet<String> currentTokens = new HashSet<>();
  private final Queue<String> poolTokens = new ConcurrentLinkedDeque<>();

  public GptTokenService() {
    currentTokens.addAll(Arrays.stream(apiKey.split(" ")).toList());
    poolTokens.addAll(currentTokens);
  }

  /**
   * Gets token from pool.
   *
   * @return token
   */
  public String getToken() {
    if (poolTokens.isEmpty()) {
      throw new RuntimeException("Pool of tokens is empty");
    }
    return poolTokens.poll();
  }

  /**
   * Gives back token to pool.
   *
   * @param token token
   */
  public void giveBackToken(String token) {
    if (currentTokens.contains(token)) {
      poolTokens.add(token);
    } else {
      throw new RuntimeException("This token is not present");
    }
  }

}
