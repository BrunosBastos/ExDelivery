package tqs.exdelivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.security.core.Transient;

import javax.persistence.*;

@Entity
@Table
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonSerialize
@Transient
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long userId;

  @Column(nullable = false, length = 50)
  private String email;

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 30)
  private String name;

  @Column(nullable = false)
  private boolean isSuperUser;

  @OneToOne(mappedBy = "user")
  private Courier courier;

  public User(String email, String password, String name, boolean isSuperUser, Courier courier) {
    this.email = email;
    this. password = password;
    this.name = name;
    this. isSuperUser = isSuperUser;
    this.courier = courier;
  }
}
