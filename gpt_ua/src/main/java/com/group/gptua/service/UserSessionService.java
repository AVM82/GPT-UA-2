package com.group.gptua.service;

import static java.time.LocalDateTime.now;

import com.group.gptua.model.GptToken;
import com.group.gptua.model.UserSession;
import com.group.gptua.utils.NoFreeTokenException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
  public UserSession getUserSession(String userHash) throws NoFreeTokenException {

    UserSession userSession = userSessions.get(userHash);
    if (userSession == null) {
      try {
        userSession = createSession(userHash);
      } catch (NoFreeTokenException e) {
        long timeForNextAttempt = getTimeForNextAttempt();
        String message = String.format("Немає вільного місця, приходьте через %s хвилин %s секунд",
            timeForNextAttempt/60, timeForNextAttempt%60);
        log.info(message);
        throw new NoFreeTokenException(message);
      }
    } else {
      userSession.setStartAt(now());
      log.info("Update user session for user hash {} at {}", userHash, userSession.getStartAt());
    }
    return userSession;

  }

  /**
   * Returns time for next attempt.
   *
   * @return A time in seconds for next attempt
   */
  private long getTimeForNextAttempt() {
    LocalDateTime timeForNextAttempt = userSessions.values().stream()
        .map(v -> v.getStartAt().plusSeconds(sessionTimeLimit))
        .min(LocalDateTime::compareTo).orElse(now());
    return ChronoUnit.SECONDS.between(now(), timeForNextAttempt);
  }

  /**
   * Creates a new user session.
   *
   * @param userHash A user hash
   * @return A new user session
   */
  private UserSession createSession(String userHash) throws NoFreeTokenException {

    UserSession userSession = new UserSession(userHash, gptTokenService.getToken(),
        now());
    userSessions.put(userHash, userSession);
    log.info("Create new user session for user hash {} at {}", userHash, userSession.getStartAt());
    return userSession;

  }

  /**
   * Runs a scheduled task for finish sessions by time limit.
   */
  @Scheduled(cron = "${user.session.time.limit.cron}")
  private void finishSessionsByTimeLimit() {

    List<Entry<String, UserSession>> entries = userSessions.entrySet().stream()
        .filter(entry -> entry.getValue().getStartAt().plusSeconds(sessionTimeLimit)
            .isBefore(now()))
        .toList();
    entries.forEach(entry -> finishSession(entry.getKey(), entry.getValue().getToken()));

  }

  /**
   * Finishes session.
   *
   * @param key   A hash user
   * @param token A token
   */
  private void finishSession(String key, GptToken token) {

    log.info("Finish user session with hash {}, finished {}", key, now());
    userSessions.remove(key);
    try {
      gptTokenService.giveBackToken(token);
      log.info("Give back token successfully");
    } catch (Exception e) {
      log.error("Error to give back token {}", e.getMessage());
    }

  }

  /**
   * Returns count of user sessions.
   *
   * @return A count of user sessions
   */
  public int getUserSessionCount() {
    return userSessions.size();
  }

}
