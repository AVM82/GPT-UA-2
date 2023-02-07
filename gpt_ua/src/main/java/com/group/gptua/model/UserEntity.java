package com.group.gptua.model;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "app_users")
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

  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
  Set<UserRequestEntity> userRequestEntities;

}
