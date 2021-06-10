package tqs.exdelivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.security.core.Transient;

import javax.persistence.*;

@Entity
@Data
@JsonSerialize
@Transient
public class Courier {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long courierId;

  @Column
  private double reputation;

  @Column(nullable = false)
  private double lat;

  @Column(nullable = false)
  private double lon;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  @JsonIgnore
  private User user;
}
