package com.group.gptua.controllers;

import com.group.gptua.bot.Bot;
import com.group.gptua.dto.ApiDto;
import com.group.gptua.dto.DtoMessage;
import com.group.gptua.service.GptMessageServiceInt;
import com.group.gptua.service.OpenAiService;
import com.group.gptua.service.UserSessionServiceInt;
import com.group.gptua.utils.ControllerUtils;
import com.group.gptua.utils.Models;
import com.group.gptua.utils.NoFreeTokenException;
import com.group.gptua.utils.Translater;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @Autowired
  Translater translater;
  @Qualifier("gptMessageService")
  @Autowired
  GptMessageServiceInt gptMessageService;

  @Qualifier("userSessionService")
  @Autowired
  UserSessionServiceInt userSessionService;
  private final OpenAiService openAiClient;

  public Controller(OpenAiService openAiClient) {
    this.openAiClient = openAiClient;
  }

  /**
   * The method for Post mapping for messages.
   * @param message the message
   * @return string
   */
  @PostMapping
  @Operation(summary = "Message-method", description = "this method for messaging")
  public ResponseEntity<?> getMessage(@RequestBody DtoMessage message,
      HttpServletRequest request) {
    log.info("Message: {} ", message);
    String userHash = ControllerUtils.getHash(request);
    return ResponseEntity.status(HttpStatus.OK)
        .header("user-hash",userHash)
        .body(gptMessageService.getAnswer(userHash,
            new DtoMessage(message.getMessage(), message.getModel())));

  }

  /**
   * The method for Post mapping for translate messages.
   * @param message the message
   * @return string
   */
  @PostMapping("/translate")
  @Operation(summary = "Translate-method", description = "this method for translation")
  public ResponseEntity<?> translateMessage(@RequestBody DtoMessage message,
      HttpServletRequest request) {
    return getMessage(translater.translateToEnglish(message),request);
  }

  /**
   * Lists the currently available models, and provides basic information about each one such as the
   * owner and availability.
   *
   * @return - list of all available models
   */

  @GetMapping("/models")
  @Operation(summary = "getAllModels method", description = "this method return all models")
  public ResponseEntity<String> getAllModels() {
    try {
      return ResponseEntity.ok(openAiClient.getAllModels(userSessionService
          .getUserSession("ControllerGetAllModels")));
    } catch (NoFreeTokenException e) {
      return ResponseEntity.ok(e.getMessage());
    }
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
  @Operation(summary = "getModel method", description = "this method return available model")
  public ResponseEntity<String> getModel(@PathVariable String model) {
    try {
      return ResponseEntity.ok(openAiClient.getModel(userSessionService
          .getUserSession("ControllerGetModel"), model));
    } catch (NoFreeTokenException e) {
      return ResponseEntity.ok(e.getMessage());
    }
  }

  /**
   * The method returns the base models for the user.
   *
   * @return - base models for the user
   */
  @GetMapping("/basic_models")
  public ResponseEntity<List<Models>> getBasicModels() {
    return ResponseEntity.ok(openAiClient.getModels());
  }

  /**
   * The method sends a request to GPT Chat from the API and returns a response.
   *
   * @param apiDto        - DTO that includes a request model and a question
   * @param bindingResult - object containing validation errors
   * @return - response from GPT Chat
   */
  @PostMapping("/completions")
  @Operation(summary = "get response from GPT Chat",
      description = "this method return text response from GPT Chat")
  @RateLimiter(name = "testEndpoint",fallbackMethod = "fallBackResponse")
  public ResponseEntity<String> getAnswer(@RequestBody ApiDto apiDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body("BAD REQUEST! Please input valid ApiDto");
    }
    try {
      return ResponseEntity.ok(openAiClient.getTextMessage(
          userSessionService.getUserSession("ControllerGetAnswer"), apiDto.getModel(),
          apiDto.getPrompt()));
    } catch (Exception e) {
      return ResponseEntity.ok(e.getMessage());
    }
  }

  private ResponseEntity<String> fallBackResponse(Exception e) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body("To many requests, please wait!");
  }
}