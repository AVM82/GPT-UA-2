package com.group.gptua.controllers;

import com.group.gptua.dto.FilterParamDto;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.service.UserRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    List<UserRequestEntity> listOfRequests = requestService.getAll();
    Collections.reverse(listOfRequests);
    List<UserRequestEntity> filteredListOfRequests = new ArrayList<>();
    listOfRequests
        .stream()
        .filter(x -> hasFilterParams(x, filter))
        .limit(100)
        .forEach(filteredListOfRequests::add);
    return filteredListOfRequests;
  }

  /**
   * This private method for taking into account null values of filtering parameters.
   *
   * @param request request from DB;
   * @param filter  filter parameters;
   * @return true if request has to be printed.
   */
  private boolean hasFilterParams(UserRequestEntity request, FilterParamDto filter) {
    // if filter has no hash or hash in request == hash in filter
    boolean user = filter.getUserHash() == null
        || filter.getUserHash().equals("")
        || request.getUserHash().equals(filter.getUserHash());
    // if filter has no model or model in request == model in filter
    boolean model = filter.getModel() == null
        || request.getModel().equals(filter.getModel());
    // if filter has no text or text in request == text in filter
    boolean text = filter.getText() == null
        || filter.getText().equals("")
        || request.getRequest().contains(filter.getText());
    // if filter has no "dateFrom" and has no "dateTo"
    boolean date = (filter.getDateFrom() == null && filter.getDateTo() == null)
        // or date in request == "dateFrom" in filter
        || (filter.getDateFrom() != null
        && request.getCreatedAt().toLocalDate().isEqual(filter.getDateFrom()))
        // or date in request == "dateTo" in filter
        || (filter.getDateTo() != null
        && request.getCreatedAt().toLocalDate().isEqual(filter.getDateTo()))
        // or "dateFrom" > date in request > "dateTo"
        || (filter.getDateFrom() != null && filter.getDateTo() != null)
        && (request.getCreatedAt().toLocalDate().isAfter(filter.getDateFrom())
        && request.getCreatedAt().toLocalDate().isBefore(filter.getDateTo()));
    return user && model && text && date;
  }
}