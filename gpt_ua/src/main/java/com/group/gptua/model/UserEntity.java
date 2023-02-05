package com.group.gptua.model;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @NotBlank
  @Column(unique = true, nullable = false)
  private String login;

  @NotBlank
  @Column(nullable = false)
  private String password;

  private Long telegramId;

  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
  Set<UserRequestEntity> userRequestEntities;

}
