package com.group.gptua.repository;

import com.group.gptua.model.UserRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRequestRepository extends JpaRepository<UserRequestEntity, Long> {

}
