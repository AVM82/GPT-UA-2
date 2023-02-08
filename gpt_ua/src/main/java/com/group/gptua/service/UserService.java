package com.group.gptua.service;

import com.group.gptua.model.UserEntity;
import com.group.gptua.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Deprecated
@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Gets all user entity.
   *
   * @return list of user entities
   */
  public List<UserEntity> getAll() {
    return userRepository.findAll();
  }

  /**
   * Gets user entity by id.
   *
   * @param id user id
   * @return user entity
   */
  public UserEntity findById(String id) {
    return userRepository.findById(id).orElseThrow();
  }

  /**
   * Gets user entity by login.
   *
   * @param login login of user
   * @return user entity
   */
  public UserEntity findByLogin(String login) {
    return userRepository.findByLogin(login).orElseThrow();
  }

  /**
   * Gets user entity by login and password.
   *
   * @param login    login of user
   * @param password password of user
   * @return user entity
   */
  public UserEntity findByLoginAndPassword(String login, String password) {
    return userRepository.findByLoginAndPassword(login, password).orElseThrow();
  }

  /**
   * Gets user entity by hash.
   *
   * @param hash hash of user
   * @return user entity
   */
  public UserEntity findByHash(String hash) {
    return userRepository.findByHash(hash).orElseThrow();
  }

  /**
   * Creates new user entity.
   *
   * @param userEntity user entity
   * @return created user entity
   */
  public UserEntity create(UserEntity userEntity) {
    log.info("User create: {}", userEntity);
    userEntity.setCreatedAt(LocalDateTime.now());
    UserEntity result = userRepository.save(userEntity);
    log.info("User created successfully: {}", result);
    return result;
  }

  /**
   * Updates user entity.
   *
   * @param id         id of user entity
   * @param userEntity user entity with new fields
   * @return updated user entity
   */
  public UserEntity update(String id, UserEntity userEntity) {
    log.info("User update: {}", userEntity);
    UserEntity updatedUser = userRepository.findById(id).orElseThrow();
    updatedUser.setName(userEntity.getName());
    updatedUser.setLogin(userEntity.getLogin());
    updatedUser.setPassword(userEntity.getPassword());
    updatedUser.setTelegramId(userEntity.getTelegramId());
    log.info("Set fields for user: {}", updatedUser);
    UserEntity result = userRepository.save(updatedUser);
    log.info("User updated successfully: {}", result);
    return result;
  }

}
