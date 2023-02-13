package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class UserSessionServiceTest {

  private static final String API_KEYS = "token1 token2 token3 token4";
  private static final Logger log = LoggerFactory.getLogger(UserSessionServiceTest.class);
  private static final String[] arrayTokens = API_KEYS.split(" ");
  private static final UserSessionService userSessionService =
      new UserSessionService(new GptTokenService(API_KEYS));

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({"testUserHash1", "testUserHash2", "testUserHash3", "testUserHash4"})
  void getUserSession(String userHash) {

    int userSessionCount = userSessionService.getUserSessionCount();

    log.info("Get session for {} twice", userHash);
    assertEquals(arrayTokens[userSessionCount],
        userSessionService.getUserSession(userHash).getToken().getValue());
    assertEquals(arrayTokens[userSessionCount],
        userSessionService.getUserSession(userHash).getToken().getValue());

    // checks count of user sessions
    log.info("Count of sessions {}", userSessionService.getUserSessionCount());
    assertEquals(userSessionCount + 1, userSessionService.getUserSessionCount());

  }
}