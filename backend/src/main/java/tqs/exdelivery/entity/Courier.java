package tqs.exdelivery.entity;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Entity
@Data
@JsonSerialize
public class Courier {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long courierId;

  @Column private double reputation;

  @Column(nullable = false)
  private double lat;

  @Column(nullable = false)
  private double lon;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;
}
