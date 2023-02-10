package com.group.gptua.service;

import com.group.gptua.model.GptToken;
import com.group.gptua.model.UserSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserSessionService implements UserSessionServiceInt {

  private final Map<String, UserSession> userSessions = new HashMap<>();

  @Qualifier("gptTokenService")
  private final GptTokenServiceInt gptTokenService;

  @Value("${user.session.time.limit}")
  private Long sessionTimeLimit;

  public UserSessionService(GptTokenServiceInt gptTokenService) {
    this.gptTokenService = gptTokenService;
  }

  /**
   * Gets user session.
   *
   * @param userHash A user hash
   * @return A user session
   */
  @Override
  public UserSession getUserSession(String userHash) {

    return userSessions.getOrDefault(userHash, createSession(userHash));

  }

  /**
   * Creates a new user session.
   *
   * @param userHash A user hash
   * @return A new user session
   */
  private UserSession createSession(String userHash) {

    UserSession userSession = new UserSession(userHash, gptTokenService.getToken(),
        LocalDateTime.now());
    userSessions.put(userHash, userSession);
    log.info("Create new user session for user hash {} at {}",userHash, userSession.getStartAt());
    return userSession;

  }

  /**
   * Runs a scheduled task for finish sessions by time limit.
   */
  @Scheduled(cron = "${user.session.time.limit.cron}")
  private void finishSessionsByTimeLimit() {

    userSessions.entrySet().stream()
        .filter(entry -> entry.getValue().getStartAt().plusSeconds(sessionTimeLimit)
            .isAfter(LocalDateTime.now()))
        .forEach(entry -> finishSession(entry.getKey(), entry.getValue().getToken()));

  }

  /**
   * Finishes session.
   *
   * @param key   A hash user
   * @param token A token
   */
  private void finishSession(String key, GptToken token) {

    log.info("Finish user session with hash {}, finished {}", key, LocalDateTime.now());
    userSessions.remove(key);
    try {
      gptTokenService.giveBackToken(token);
      log.info("Give back token successfully");
    } catch (Exception e) {
      log.error("Error to give back token {}", e.getMessage());
    }

  }

}
