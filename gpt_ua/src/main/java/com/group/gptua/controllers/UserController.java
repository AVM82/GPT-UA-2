package com.group.gptua.controllers;

import com.group.gptua.dto.UserDto;
import com.group.gptua.service.UserService;
import com.group.gptua.utils.UserDtoParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Method for create an User.
   * @param userDto the userDto got to front
   * @return the userDto after saved on DB.
   */
  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
    var dbEntity = userService.findByHash(userDto.getHash());
    var entity = dbEntity == null
        ? userService.create(UserDtoParser.dtoToEntity(userDto)) :
        dbEntity;
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(UserDtoParser.entityToDto(entity));
  }

  /**
   * Method for create an User.
   * @param userDto the userDto got to front
   * @return the userDto after saved on DB.
   */
  @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
    var entity = userService.findByHash(userDto.getHash());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(UserDtoParser.entityToDto(entity));
  }

}
