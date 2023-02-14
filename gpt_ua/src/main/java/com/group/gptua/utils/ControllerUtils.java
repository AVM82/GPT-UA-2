package com.group.gptua.utils;

import java.time.LocalTime;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerUtils {

  /**
   * Method for check or create 'user-hash' headers.
   * @param request input request.
   * @return string 'user-hash'.
   */
  public static String getHash(HttpServletRequest request) {
    String userHash = request.getHeader("user-hash");
    log.info("UserHash getting: {} ", userHash);
    if (userHash == null || userHash.isEmpty()) {
      userHash = Base64.getEncoder().encodeToString(
          (LocalTime.now().getNano() + "{|}" + request.getHeader("referer")
              + "{|}" + request.getHeader("user-agent"))
              .replaceAll(" ", "").getBytes());
    }
    log.info("UserHash for response: {} ", userHash);
    return userHash;
  }

}
