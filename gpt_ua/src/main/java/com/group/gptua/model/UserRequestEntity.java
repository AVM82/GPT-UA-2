package com.group.gptua.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "app_user_requests")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserRequestEntity {

  @Id
  private String id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  private String request;
  private String response;
  private LocalDateTime createdAt;

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
    userEntity.getUserRequestEntities().add(this);
  }
}
