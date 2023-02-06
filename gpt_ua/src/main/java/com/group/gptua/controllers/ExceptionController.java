package com.group.gptua.controllers;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

  /**
   * The method returns a corresponding message if a model was entered that is not in the Models.
   * enum
   *
   * @return - error message
   */
  @ResponseBody
  @ExceptionHandler(HttpMessageNotReadableException.class)
  String modelNotValueHandler() {
    return "ERROR! Model is not valid. Please enter values accepted "
        + "for Enum class: [BABBAGE, CURIE, ADA, DAVINCI]";
  }
}
