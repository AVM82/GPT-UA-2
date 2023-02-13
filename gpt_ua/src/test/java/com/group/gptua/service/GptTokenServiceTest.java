package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.group.gptua.model.GptAccount;
import com.group.gptua.model.GptToken;
import com.group.gptua.utils.NoFreeTokenException;
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
  private static final Logger log = LoggerFactory.getLogger(GptTokenServiceTest.class);
  private static final String[] arrayTokens = API_KEYS.split(" ");
  public static final int COUNT_TOKENS = 5;

  @Test
  @Order(1)
  void getToken() {
    log.info("start to get tokens from pool");
    for (int i = 0; i < arrayTokens.length + COUNT_TOKENS; i++) {
      try {
        receivedTokens.add(gptTokenService.getToken());
      } catch (Exception e) {
        assertEquals(NoFreeTokenException.class, e.getClass());
      }
    }
    log.info("start to check tokens");
    for (int i = 0; i < receivedTokens.size() - 1; i++) {
      // is token in pool
      assertEquals(arrayTokens[i], receivedTokens.get(i).getValue());
    }
  }

  @Test
  @Order(2)
  void giveBackToken() {
    for (int i = 0; i < receivedTokens.size() - 1; i++) {
      try {
        gptTokenService.giveBackToken(receivedTokens.get(i));
        assertTrue(true);
      } catch (Exception e) {
        fail();
      }
    }
    log.info("checks pool size after giving back tokens");
    assertEquals(receivedTokens.size() - 1, gptTokenService.getPoolTokensSize());
  }

  @Test
  @Order(3)
  void giveBackInvalidToken() {
    log.info("start check giveBackInvalidToken");
    assertThrows(Exception.class,
        () -> gptTokenService.giveBackToken(new GptToken("Invalid", new GptAccount(""))));
  }

}