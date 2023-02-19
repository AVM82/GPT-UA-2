package com.group.gptua.controllers;

import com.group.gptua.bot.Bot;
import com.group.gptua.dto.ApiDto;
import com.group.gptua.dto.ApiWithMoodDto;
import com.group.gptua.dto.DtoMessage;
import com.group.gptua.model.Moods;
import com.group.gptua.service.GptMessageServiceInt;
import com.group.gptua.service.OpenAiService;
import com.group.gptua.service.UserSessionServiceInt;
import com.group.gptua.utils.ControllerUtils;
import com.group.gptua.utils.Models;
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
import org.springframework.web.bind.annotation.GetMapping;
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
   *
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
        .header("user-hash", userHash)
        .body(gptMessageService.getAnswer(userHash,
            new DtoMessage(message.getMessage(), message.getModel())));
  }

  /**
   * The method for Post mapping for translate messages to English.
   *
   * @param message the message
   * @return string
   */
  @PostMapping("/translate/en")
  @Operation(summary = "Translate-method", description = "this method for translation")
  public ResponseEntity<?> translateMessageEn(@RequestBody DtoMessage message,
      HttpServletRequest request) {
    return getMessage(translater.translateToEnglish(message), request);
  }

  @PostMapping("/translate/uk")
  @Operation(summary = "Translate-method", description = "this method for translation")
  public ResponseEntity<?> translateMessageUk(@RequestBody DtoMessage message,
      HttpServletRequest request) {
    return getMessage(translater.translateToUkraine(message), request);
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
   * The method returns the available mood styles.
   *
   * @return -
   */
  @GetMapping("/moods")
  public ResponseEntity<List<Moods>> getMoods() {
    return ResponseEntity.ok(openAiClient.getMoods());
  }

  /**
   * The method returns a response modified by GPT chat depending on the given mood.
   *
   * @param apiWithMoodDto - dto which consist mood;
   * @return - DtoMessage
   */
  @PostMapping("/completions/mood")
  @RateLimiter(name = "mood message endpoint", fallbackMethod = "fallBackResponse")
  public ResponseEntity<?> getAnswerOfMood(@RequestBody ApiWithMoodDto apiWithMoodDto,
      HttpServletRequest request) {
    try {
      ApiDto moodDto = openAiClient.setMessage(apiWithMoodDto);
      String userHash = ControllerUtils.getHash(request);
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .header("user-hash", userHash)
          .body(gptMessageService.getAnswer(userHash,
              new DtoMessage(moodDto.getPrompt(), moodDto.getModel())));
    } catch (Exception e) {
      return ResponseEntity.ok(e.getMessage());
    }
  }
}