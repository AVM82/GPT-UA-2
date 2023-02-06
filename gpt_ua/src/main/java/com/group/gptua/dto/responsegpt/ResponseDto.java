package com.group.gptua.dto.responsegpt;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

  private String id;
  private String object;
  private String created;
  private String model;
  private List<Choice> choices;
  private Usage usage;
}
