package com.group.gptua.bot.dto;

import com.group.gptua.dto.DtoMessage;
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
public class BotButtonDto {

  String buttonName;

  DtoMessage dtoMessage;

}
