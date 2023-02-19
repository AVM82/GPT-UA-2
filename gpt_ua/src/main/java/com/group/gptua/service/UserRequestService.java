package com.group.gptua.service;

import com.group.gptua.dto.FilterParamDto;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.repository.UserRequestRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserRequestService {

  private final UserRequestRepository userRequestRepository;

  public UserRequestService(UserRequestRepository userRequestRepository) {
    this.userRequestRepository = userRequestRepository;
  }

  /**
   * Get all user request entities.
   *
   * @return list of user request entities
   */
  public List<UserRequestEntity> getAll() {
    return userRequestRepository.findAll();
  }

  /**
   * Get user request entities by user hash.
   *
   * @return list of user request entities
   */
  public List<UserRequestEntity> findByUserHash(String userHash) {
    return userRequestRepository.findByUserHash(userHash);
  }

  /**
   * Creates user request entity.
   *
   * @param userRequestEntity user request entity
   * @return created user request entity
   */
  public UserRequestEntity create(UserRequestEntity userRequestEntity) {
    log.info("User request create: {}", userRequestEntity);
    userRequestEntity.setCreatedAt(LocalDateTime.now());
    UserRequestEntity result = userRequestRepository.save(userRequestEntity);
    log.info("User request created successfully: {}", result);
    return result;
  }

  /** This is method for filtering requests by: -user; -model; -text of request; - date.
   *
   * @param filter - dto with filter parameters;
   * @return list of requests.
   */
  public List<UserRequestEntity> getFilteredRequests(FilterParamDto filter) {
    List<UserRequestEntity> listOfRequests = getAll();
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
