package com.group.gptua.dto;

import com.group.gptua.model.Moods;
import com.group.gptua.utils.Models;
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
public class ApiWithMoodDto {

  private Models model;
  private String message;
  private Moods mood;

  @Override
  public String toString() {
    return "ApiWithMoodDto{"
        + "model=" + model
        + ", message='" + message + '\''
        + ", mood=" + mood
        + '}';
  }
}
