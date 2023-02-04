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
public class RequestBodyDto {

  @NotEmpty
  private String model;
  @NotEmpty
  private String prompt;
  @Min(0)
  @Max(10)
  @JsonProperty(value = "max_tokens")
  private int maxTokens;
  @Min(0)
  @Max(10)
  private int temperature;

  @Override
  public String toString() {
    return "ResponseDto{"
        + "model='" + model + '\''
        + ", prompt='" + prompt + '\''
        + ", maxTokens=" + maxTokens
        + ", temperature=" + temperature
        + '}';
  }
}
