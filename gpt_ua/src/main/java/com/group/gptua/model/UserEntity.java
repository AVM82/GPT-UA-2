package com.group.gptua.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Deprecated
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserEntity {

  @Id
  private String id;

  private String name;

  private String login;

  private String password;

  private Long telegramId;

  private String hash;

  private LocalDateTime createdAt;

}
