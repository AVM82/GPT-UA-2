package com.group.gptua.controllers;

import com.group.gptua.dto.FilterParamDto;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.service.UserRequestService;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/archive")
@Slf4j
public class RequestController {

  UserRequestService requestService;

  public RequestController(UserRequestService requestService) {
    this.requestService = requestService;
  }

  /**
   * This is controller.
   *
   * @return list
   */
  @GetMapping("/filter")
  public List<UserRequestEntity> getFilteredTasks(FilterParamDto filter) {
    List<UserRequestEntity> list = new ArrayList<>();
    requestService.getAll()
        .stream()
        .filter(x -> hasFilterParams(x, filter))
        .limit(100)
        .forEach(list::add);
    return list;
  }

  /**
   * This method.
   *
   * @param request request
   * @param filter  filter
   * @return boolean
   */
  private boolean hasFilterParams(UserRequestEntity request, FilterParamDto filter) {
    boolean user = filter.getUserHash() == null
        || filter.getUserHash().equals("")
        || request.getUserHash().equals(filter.getUserHash());
    boolean model = filter.getModel() == null
        || request.getModel().equals(filter.getModel());
    boolean text = filter.getText() == null
        || filter.getText().equals("")
        || request.getRequest().contains(filter.getText());
    boolean date = (filter.getDateFrom() == null && filter.getDateTo() == null)
        || (filter.getDateFrom() != null
        && request.getCreatedAt().toLocalDate().isEqual(filter.getDateFrom()))
        || (filter.getDateTo() != null
        && request.getCreatedAt().toLocalDate().isEqual(filter.getDateTo()))
        || (filter.getDateFrom() != null && filter.getDateTo() != null)
        && (request.getCreatedAt().toLocalDate().isAfter(filter.getDateFrom())
        && request.getCreatedAt().toLocalDate().isBefore(filter.getDateTo()));
    return user && model && text && date;
  }

  /**
   * This is controller.
   *
   * @return list
   */
  @GetMapping("/")
  public List<UserRequestEntity> getAllTasks() {
    return requestService.getAll();
  }
}