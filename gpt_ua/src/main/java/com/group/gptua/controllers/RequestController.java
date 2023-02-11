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

  /** This method.
   *
   * @param request request
   * @param filter filter
   * @return boolean
   */
  private boolean hasFilterParams(UserRequestEntity request, FilterParamDto filter) {
    boolean model = filter.getModel() == null
        || request.getModel().equals(filter.getModel());
    boolean text = filter.getText() == null
        || request.getRequest().contains(filter.getText());
    boolean date = filter.getDate() == null
        || request.getCreatedAt().toLocalDate().equals(filter.getDate());
    return model && text && date;
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