package com.group.gptua.controllers;

import com.group.gptua.bot.Bot;
import com.group.gptua.dto.ApiDto;
import com.group.gptua.dto.DtoMessage;
import com.group.gptua.service.GptMessageServiceInt;
import com.group.gptua.service.OpenAiService;
import com.group.gptua.utils.Models;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalTime;
import java.util.Base64;
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

  @Qualifier("gptMessageService")
  @Autowired
  GptMessageServiceInt gptMessageService;

  private final OpenAiService openAiClient;

  public Controller(OpenAiService openAiClient) {
    this.openAiClient = openAiClient;
  }

  /**
   * The method for GET mapping.
   *
   * @param message the message
   * @return string
   */
  @PostMapping
  @Operation(summary = "getEcho-method", description = "this method tests controller")
  public ResponseEntity<?> getMessage(@RequestBody DtoMessage message,
      HttpServletRequest request) {
    log.info("Message: {} ", message);
    String userHash = request.getHeader("user-hash");
    log.info("UserHash getting: {} ", userHash);
    if (userHash.isEmpty()) {
      userHash = Base64.getEncoder().encodeToString(
          (LocalTime.now().getNano() + "{|}" + request.getHeader("referer")
              + "{|}" + request.getHeader("user-agent"))
              .replaceAll(" ", "").getBytes());
    }
    log.info("UserHash for response: {} ", userHash);
    return ResponseEntity.status(HttpStatus.OK)
        .header("user-hash",userHash)
        .body(gptMessageService.getAnswer(userHash,
            new DtoMessage(message.getMessage(), Models.ADA)));
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
    return ResponseEntity.ok(openAiClient.getAllModels());
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
    return ResponseEntity.ok(openAiClient.getModel(model));
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
    return ResponseEntity.ok(openAiClient.getTextMessage(apiDto.getModel(),
        apiDto.getPrompt()));
  }

  private ResponseEntity<String> fallBackResponse(Exception e) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body("To many requests, please wait!");
  }
}