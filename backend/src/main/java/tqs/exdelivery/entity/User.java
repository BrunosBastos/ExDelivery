package tqs.exdelivery.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.Transient;

import javax.persistence.*;

@Entity
@Table
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonSerialize
@Transient
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long userId;

  @Column(nullable = false, length = 50)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 30)
  private String name;

  @Column(nullable = false)
  private boolean isSuperUser;

  @OneToOne(mappedBy = "user")
  private Courier courier;
}
