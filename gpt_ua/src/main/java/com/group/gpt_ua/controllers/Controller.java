package com.group.gpt_ua.controllers;


import com.group.gpt_ua.bot.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
@Slf4j
public class Controller {
  @Autowired
  Bot bot;


  /**
   * The method for GET mapping
   * @param message the message
   * @return string
   */
  @GetMapping
  public ResponseEntity<?> index(@RequestParam(name = "mess", required = false) String message) {
    log.info("Message: {} ",message);
    bot.sendTextMessage(Bot.chatIDLast, message);
    return ResponseEntity.ok(message);
  }

}
