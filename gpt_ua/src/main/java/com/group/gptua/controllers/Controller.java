package com.group.gptua.controllers;

import com.group.gptua.bot.Bot;
import com.group.gptua.dto.DtoMessage;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition(info = @Info(title = "GPT-UA",
    version = "${api.ver}",
    description = "This is the application provides GPT-chat access..."))
@Tag(name = "Controller API", description = "This controller manages the GPT chat...")
@RequestMapping("/bot")
@Slf4j
public class Controller {
  @Autowired
  Bot bot;

  /**
   * The method for GET mapping.
   * @param message the message
   * @return string
   */
  @GetMapping
  @Operation(summary = "getEcho-method", description = "this method tests controller")
  public ResponseEntity<?> index(@RequestParam(name = "mess", required = false) String message) {
    log.info("Message: {} ",message);
    if (Bot.chatIDLast != null) {
      bot.sendTextMessage(Bot.chatIDLast, message);
    }
    return ResponseEntity.ok(new DtoMessage(message));
  }

}
