package com.group.gptua.dto;

import com.group.gptua.utils.Models;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterParamDto {
  String userHash;
  Models model;
  String text;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDate dateFrom;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDate dateTo;
}
