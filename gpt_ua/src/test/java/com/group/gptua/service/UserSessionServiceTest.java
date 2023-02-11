package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class UserSessionServiceTest {

  private static final String API_KEYS = "token1 token2 token3 token4";
  private static final Logger log = LoggerFactory.getLogger(UserSessionServiceTest.class);

  private static final UserSessionService userSessionService =
      new UserSessionService(new GptTokenService(API_KEYS));

  @SneakyThrows
  @Test
  void getUserSession() {

    String[] arrayTokens = API_KEYS.split(" ");
    String userHash = "testUserHash1";
    log.info("Get session for {} twice", userHash);
    assertEquals(arrayTokens[1], userSessionService.getUserSession(userHash).getToken().getToken());
    assertEquals(arrayTokens[1], userSessionService.getUserSession(userHash).getToken().getToken());

    userHash = "testUserHash2";
    log.info("Get session for {} twice", userHash);
    assertEquals(arrayTokens[2], userSessionService.getUserSession(userHash).getToken().getToken());
    assertEquals(arrayTokens[2], userSessionService.getUserSession(userHash).getToken().getToken());

    userHash = "testUserHash3";
    log.info("Get session for {} twice", userHash);
    assertEquals(arrayTokens[3], userSessionService.getUserSession(userHash).getToken().getToken());
    assertEquals(arrayTokens[3], userSessionService.getUserSession(userHash).getToken().getToken());

    // default token
    userHash = "testUserHash4";
    log.info("Get session for {} twice", userHash);
    assertEquals(arrayTokens[0], userSessionService.getUserSession(userHash).getToken().getToken());
    assertEquals(arrayTokens[0], userSessionService.getUserSession(userHash).getToken().getToken());

    // checks count of user sessions
    int userSessionCount = userSessionService.getUserSessionCount();
    log.info("Count of sessions {}", userSessionCount);
    assertEquals(4, userSessionCount);

  }
}