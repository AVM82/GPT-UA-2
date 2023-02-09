package com.group.gptua.dto;

import com.group.gptua.utils.Models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoMessage {
  private String message;
  private Models model;
  private String userHash;
}
