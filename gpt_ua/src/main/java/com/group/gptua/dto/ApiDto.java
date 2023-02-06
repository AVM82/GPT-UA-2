package com.group.gptua.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group.gptua.utils.Models;
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
public class ApiDto {

  @NotEmpty
  private Models model;
  @NotEmpty
  private String prompt;

  @Override
  public String toString() {
    return "ApiDto{"
        + "model=" + model
        + ", prompt='" + prompt + '\''
        + '}';
  }
}
