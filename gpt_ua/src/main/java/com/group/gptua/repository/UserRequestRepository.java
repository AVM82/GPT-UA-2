package com.group.gptua.repository;

import com.group.gptua.model.UserRequestEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRequestRepository extends MongoRepository<UserRequestEntity, String> {

  List<UserRequestEntity> findByUserHash(String userHash);

}
