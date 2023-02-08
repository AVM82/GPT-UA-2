package com.group.gptua.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

  @NotEmpty
  private String model;
  @NotEmpty
  private String prompt;
  @Min(0)
  @Max(2097)
  @JsonProperty(value = "max_tokens")
  private int maxTokens;
  @Min(0)
  @Max(1)
  private double temperature;
  @Min(0)
  @Max(1)
  @JsonProperty(value = "top_p")
  private double topP;
  @Min(0)
  @Max(1)
  @JsonProperty(value = "frequency_penalty")
  private double frequencyPenalty;
  @Min(0)
  @Max(1)
  @JsonProperty(value = "presence_penalty")
  private double presencePenalty;
  private String[] stop;

}
