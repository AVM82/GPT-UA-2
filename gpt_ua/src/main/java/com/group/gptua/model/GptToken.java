package com.group.gptua.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GptToken {

  private String token;
  private GptAccount account;

}
