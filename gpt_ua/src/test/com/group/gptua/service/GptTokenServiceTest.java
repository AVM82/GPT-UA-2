package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.*;

import com.group.gptua.model.GptAccount;
import com.group.gptua.model.GptToken;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GptTokenServiceTest {

  private static final String API_KEYS = "token1 token2 token3 token4";
  private static final GptTokenService gptTokenService = new GptTokenService(API_KEYS);
  private static final List<GptToken> receivedTokens = new ArrayList<>();
  private static final int poolTokensSize = gptTokenService.getPoolTokensSize();
  private static final Logger log = LoggerFactory.getLogger(GptTokenServiceTest.class);

  @Test
  @Order(1)
  void getToken() {
    String[] arrayTokens = API_KEYS.split(" ");
    // default token
    String defaultToken = arrayTokens[0];
    log.info("start to add tokens to pool");
    for (int i = 1; i < arrayTokens.length + 5; i++) {
      receivedTokens.add(gptTokenService.getToken());
    }
    log.info("start to check tokens");
    for (int i = 0; i < receivedTokens.size() - 1; i++) {
      if (i < arrayTokens.length - 1) {
        // is token in pool
        assertEquals(arrayTokens[i + 1], receivedTokens.get(i).getToken());
      } else {
        // is token default
        assertEquals(defaultToken, receivedTokens.get(i).getToken());
      }

    }
  }

  @Test
  @Order(2)
  void giveBackToken() {
    log.info("start check giveBackToken");
    for (int i = 0; i < receivedTokens.size() - 1; i++) {
      try {
        gptTokenService.giveBackToken(receivedTokens.get(i));
        assertTrue(true);
      } catch (Exception e) {
        fail();
      }
    }
    log.info("checks pool size after giving back tokens");
    assertEquals(poolTokensSize, gptTokenService.getPoolTokensSize());
  }

  @Test
  @Order(3)
  void giveBackInvalidToken() {
    log.info("start check giveBackInvalidToken");
    assertThrows(Exception.class,
        () -> gptTokenService.giveBackToken(new GptToken("Invalid", new GptAccount(""))));
  }

}