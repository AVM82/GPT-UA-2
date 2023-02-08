package com.group.gptua.service;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptTokenService implements GptTokenServiceInt {

  private final HashSet<String> currentTokens = new HashSet<>();
  private final Queue<String> poolTokens = new ArrayDeque<>();
  private String defaultToken;

  /**
   * Constructor for initialisation variables.
   * @param apiKey tokens in variable
   */
  @Autowired
  public GptTokenService(@Value("${gpt.token}") String apiKey) {
    currentTokens.addAll(Arrays.stream(apiKey.split(" ")).toList());
    poolTokens.addAll(currentTokens);
    defaultToken = poolTokens.poll();
  }

  /**
   * Gets token from pool.
   *
   * @return token
   */
  public String getToken() {
    if (poolTokens.isEmpty()) {
      log.info("default token");
      return defaultToken;
    }
    log.info("token from poolTokens");
    return poolTokens.poll();
  }

  /**
   * Gives back token to pool.
   *
   * @param token token
   */
  public void giveBackToken(String token) throws Exception {
    if (currentTokens.contains(token)) {
      log.info("give back token to poolTokens");
      poolTokens.add(token);
    } else {
      log.warn("default token");
      throw new Exception("This token is not present");
    }
  }

}
