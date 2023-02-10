package com.group.gptua.service;

import com.group.gptua.model.GptAccount;
import com.group.gptua.model.GptToken;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptTokenService implements GptTokenServiceInt {

  private final HashSet<GptToken> currentTokens = new HashSet<>();
  private final Queue<GptToken> poolTokens = new ArrayDeque<>();
  private final GptToken defaultToken;

  /**
   * Constructor for initialisation variables.
   *
   * @param apiKey tokens in variable
   */
  @Autowired
  public GptTokenService(@Value("${gpt.token}") String apiKey) {
    currentTokens.addAll(Arrays.stream(apiKey.split(" "))
        .map(token -> new GptToken(token, new GptAccount(""))).toList());
    poolTokens.addAll(currentTokens);
    defaultToken = poolTokens.poll();
  }

  /**
   * Gets token from pool.
   *
   * @return token
   */
  public GptToken getToken() {
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
  public void giveBackToken(GptToken token) throws Exception {
    if (currentTokens.contains(token)) {
      if (token != defaultToken) {
        log.info("give back token to poolTokens");
        poolTokens.add(token);
      } else {
        log.info("give back default token");
      }
    } else {
      log.warn("default token");
      throw new Exception("This token is not present");
    }
  }

}
