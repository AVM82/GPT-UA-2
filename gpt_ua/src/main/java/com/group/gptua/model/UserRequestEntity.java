package com.group.gptua.model;

import com.group.gptua.utils.Models;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestEntity {

  @Id
  private String id;

  private String userHash;

  private Models model;

  private String request;

  private String response;

  private LocalDateTime createdAt;

}
