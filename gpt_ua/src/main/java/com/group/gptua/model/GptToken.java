package com.group.gptua.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GptToken {

  private String value;
  private GptAccount account;

}
