package com.group.gptua.dto;

import com.group.gptua.utils.Models;
import java.time.LocalDate;
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
public class FilterParamDto {

  Models model;
  String text;
  LocalDate date;
}
