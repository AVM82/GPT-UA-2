package com.group.gptua.service;

import static java.time.LocalDateTime.now;

import com.group.gptua.model.GptAccount;
import com.group.gptua.model.GptToken;
import com.group.gptua.utils.NoFreeTokenException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GptTokenService implements GptTokenServiceInt {

  private final Set<GptToken> currentTokens = new LinkedHashSet<>();
  private final Queue<GptToken> poolTokens = new ArrayDeque<>();

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
  }

  /**
   * Gets token from pool.
   *
   * @return token
   */
  public GptToken getToken() throws NoFreeTokenException {
    if (poolTokens.isEmpty()) {
      String message = "No free token";
      log.info("{}", message);
      throw new NoFreeTokenException(message);
    }
    log.info("given token from poolTokens: {}", now());
    return poolTokens.poll();
  }

  /**
   * Gives back token to pool.
   *
   * @param token token
   */
  public void giveBackToken(GptToken token) throws Exception {
    if (currentTokens.contains(token)) {
      log.info("give back token to poolTokens: {}", now());
      poolTokens.add(token);
    } else {
      String message = "This token is not present in currentTokens";
      log.warn(message);
      throw new Exception(message);
    }
  }

  public int getPoolTokensSize() {
    return poolTokens.size();
  }

}
