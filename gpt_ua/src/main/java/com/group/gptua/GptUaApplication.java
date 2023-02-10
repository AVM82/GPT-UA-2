package com.group.gptua;

import com.group.gptua.repository.UserRepository;
import com.group.gptua.repository.UserRequestRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableMongoRepositories(basePackageClasses = {UserRepository.class, UserRequestRepository.class})
@EnableJpaRepositories(excludeFilters =
    {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserRepository.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            value = UserRequestRepository.class)})
@EnableScheduling
@SpringBootApplication
public class GptUaApplication {

  public static void main(String[] args) {
    SpringApplication.run(GptUaApplication.class, args);
  }

}
