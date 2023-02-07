package com.group.gptua.repository;

import com.group.gptua.model.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

  Optional<UserEntity> findByLogin(String login);

  Optional<UserEntity> findByLoginAndPassword(String login, String password);

  Optional<UserEntity> findByHash(String hash);

}
