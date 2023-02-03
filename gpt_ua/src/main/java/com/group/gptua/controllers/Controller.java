package com.group.gptua.controllers;

import com.group.gptua.bot.Bot;
import com.group.gptua.dto.DtoMessage;
import com.group.gptua.dto.ResponseDto;
import com.group.gptua.service.OpenAIClient;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  private final OpenAIClient openAIClient;

  @Autowired
  public Controller(OpenAIClient openAIClient) {
    this.openAIClient = openAIClient;
  }

  /**
   * The method for GET mapping.
   *
   * @param message the message
   * @return string
   */
  @GetMapping
  @Operation(summary = "getEcho-method", description = "this method tests controller")
  public ResponseEntity<?> index(@RequestParam(name = "mess", required = false) String message) {
    log.info("Message: {} ", message);
    if (Bot.chatIDLast != null) {
      bot.sendTextMessage(Bot.chatIDLast, message);
    }
    return ResponseEntity.ok(new DtoMessage(message));
  }

  // Метод повертає всі моделі з якими працює API
  @GetMapping("/models")
  public ResponseEntity<String> getAllModels() throws IOException {
    return ResponseEntity.ok(openAIClient.getModels().body().string());
  }

  // Метод повертає конкретну модель з якою працює API
  @GetMapping("/models/{model}")
  public ResponseEntity<String> getAllModels(@PathVariable Integer model) {
    return ResponseEntity.ok(" Model number is : " + model);
  }

  @PostMapping("/test")
  public ResponseEntity<String> getRequest(@RequestBody ResponseDto message) throws Exception {
    return ResponseEntity.ok(openAIClient.firstResponse(message).body().string());
  }

}
