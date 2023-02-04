package com.group.gptua.repository;

import com.group.gptua.model.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByLogin(String login);

  Optional<UserEntity> findByLoginAndPassword(String login, String password);

}
