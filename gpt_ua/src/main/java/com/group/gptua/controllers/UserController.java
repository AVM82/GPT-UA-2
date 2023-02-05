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
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
    var entity = userService.create(UserDtoParser.dtoToEntity(userDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(UserDtoParser.entityToDto(entity));
  }

}
