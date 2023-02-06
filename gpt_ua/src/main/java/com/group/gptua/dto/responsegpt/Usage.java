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
public class Usage {

  @JsonProperty("prompt_tokens")
  private Long promptTokens;
  @JsonProperty("completion_tokens")
  private Long completionTokens;
  @JsonProperty("total_tokens")
  private Long totalTokens;
}
