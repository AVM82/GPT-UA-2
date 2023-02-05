package com.group.gptua.utils;

import com.group.gptua.dto.UserDto;
import com.group.gptua.model.UserEntity;

public class UserDtoParser {

  /**
   * Method for change userDto to userEntity.
   * @param userDto Incoming the userDto.
   * @return the userEntity.
   */
  public static UserEntity dtoToEntity(UserDto userDto) {
    var entity = new UserEntity();
    entity.setName(userDto.getName());
    entity.setPassword(userDto.getPassword());
    entity.setLogin(userDto.getLogin());
    return entity;
  }

  /**
   * Method for change userEntity to userDto.
   * @param entity Incoming the userEntity.
   * @return the userDto.
   */
  public static UserDto entityToDto(UserEntity entity) {
    return new UserDto(entity.getName(), entity.getLogin(), entity.getPassword());
  }

}
