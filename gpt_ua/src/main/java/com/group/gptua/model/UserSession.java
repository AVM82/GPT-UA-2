package com.group.gptua.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSession {

  String userHash;
  GptToken token;
  LocalDateTime startAt;

}
