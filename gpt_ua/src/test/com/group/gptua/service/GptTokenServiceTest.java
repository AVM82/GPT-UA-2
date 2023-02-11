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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GptTokenServiceTest {

  private static final String API_KEYS = "token1 token2 token3 token4";
  private static final GptTokenService gptTokenService = new GptTokenService(API_KEYS);
  private static final List<GptToken> receivedTokens = new ArrayList<>();
  private static final int poolTokensSize = gptTokenService.getPoolTokensSize();

  @Test
  @Order(1)
  void getToken() {
    String[] arrayTokens = API_KEYS.split(" ");
    // default token
    String defaultToken = arrayTokens[0];
    // adds tokens to pool
    for (int i = 1; i < arrayTokens.length + 5; i++) {
      receivedTokens.add(gptTokenService.getToken());
    }
    // checks tokens
    for (int i = 0; i < receivedTokens.size() - 1; i++) {
      if (i < arrayTokens.length - 1) {
        // token is in pool
        assertEquals(arrayTokens[i + 1], receivedTokens.get(i).getToken());
      } else {
        // token is default
        assertEquals(defaultToken, receivedTokens.get(i).getToken());
      }

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
    // checks pool size after giving back tokens
    assertEquals(poolTokensSize, gptTokenService.getPoolTokensSize());
  }

  @Test
  @Order(3)
  void giveBackInvalidToken() {
    assertThrows(Exception.class,
        () -> gptTokenService.giveBackToken(new GptToken("Invalid", new GptAccount(""))));
  }

}