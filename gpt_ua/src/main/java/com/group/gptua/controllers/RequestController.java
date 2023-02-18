package com.group.gptua.controllers;

import com.group.gptua.dto.FilterParamDto;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.service.UserRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Request controller",
    description = "This controller manages the archive of the GPT chat...")
@RequestMapping("/archive")
@Slf4j
public class RequestController {

  UserRequestService requestService;

  public RequestController(UserRequestService requestService) {
    this.requestService = requestService;
  }

  /**
   * This is end point for filtering requests by: -user; -model; -text of request; - date.
   *
   * @return list of requests
   */
  @GetMapping("/filter")
  @Operation(summary = "getFilteredRequests-method",
      description = "this method filters requests by: -user; -model; -text of request; - date ")
  public List<UserRequestEntity> getFilteredRequests(FilterParamDto filter) {

    return requestService.getFilteredRequests(filter);
  }
}