package com.group.gptua.service;

import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.repository.UserRequestRepository;
import java.time.LocalDateTime;
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
   * Finds user request entity by id.
   *
   * @param id id of user request entity
   * @return user request entity
   */
  public UserRequestEntity findById(String id) {
    return userRequestRepository.findById(id).orElseThrow();
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

  /**
   * Updates user request entity.
   *
   * @param id                id of user request entity
   * @param userRequestEntity user request entity
   * @return updated user request entity
   */
  public UserRequestEntity update(String id, UserRequestEntity userRequestEntity) {
    log.info("User request update: {}", userRequestEntity);
    UserRequestEntity updatedUserRequest = userRequestRepository.findById(id).orElseThrow();
    updatedUserRequest.setHashUser(userRequestEntity.getHashUser());
    updatedUserRequest.setModel(userRequestEntity.getModel());
    updatedUserRequest.setRequest(userRequestEntity.getRequest());
    updatedUserRequest.setResponse(userRequestEntity.getResponse());
    log.info("Set fields for user request: {}", updatedUserRequest);
    UserRequestEntity result = userRequestRepository.save(updatedUserRequest);
    log.info("User request updated successfully: {}", result);
    return result;
  }

}
