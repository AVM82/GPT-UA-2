package com.group.gptua.dto.responsegpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
  private String text;
  private long index;
  private String logprobs;
  @JsonProperty("finish_reason")
  private String finishReason;
}
