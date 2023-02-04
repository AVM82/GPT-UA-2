package com.group.gptua.controllers;

import com.group.gptua.bot.Bot;
import com.group.gptua.dto.DtoMessage;
import com.group.gptua.dto.RequestBodyDto;
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

  /**
   * Lists the currently available models, and provides basic information about each one such as the
   * owner and availability.
   *
   * @return - list of all available models
   */
  @GetMapping("/models")
  @Operation(summary = "getAllModels method", description = "this method return all models from api")
  public ResponseEntity<String> getAllModels() {
    return ResponseEntity.ok(openAIClient.getModels());
  }

  /**
   * Retrieves a model instance, providing basic information about the model such as the owner and
   * permissioning. The list of available models can be obtained from the following endpoint:
   * /models
   *
   * @param model - available model
   * @return - string
   */
  @GetMapping("/models/{model}")
  @Operation(summary = "getModel method", description = "this method return available model from api")
  public ResponseEntity<String> getModel(@PathVariable String model) {
    return ResponseEntity.ok(openAIClient.getModel(model));
  }

  /**
   * Given a prompt, the model will return one or more predicted completions, and can also return
   * the probabilities of alternative tokens at each position.
   *
   * @param bodyDto - RequestBodyDto
   * @return - string
   */
  @PostMapping("/completions")
  @Operation(summary = "getResponse method", description = "this method return one or more predicted completions")
  public ResponseEntity<String> getResponse(@RequestBody RequestBodyDto bodyDto) {
    return ResponseEntity.ok(openAIClient.getResponse(bodyDto));
  }
}