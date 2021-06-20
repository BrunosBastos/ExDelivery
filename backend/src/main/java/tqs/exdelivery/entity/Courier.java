package tqs.exdelivery.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.Transient;

import javax.persistence.*;

@Entity
@Data
@JsonSerialize
@Transient
@AllArgsConstructor
@NoArgsConstructor
public class Courier {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  @ColumnDefault("2.5")
  private double reputation;

  @Column(nullable = false)
  private double lat;

  @Column(nullable = false)
  private double lon;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;

  @Column
  @ColumnDefault("true")
  private boolean active;

  public Courier(double reputation, double lat, double lon, User user) {
    this.reputation = reputation;
    this.lat = lat;
    this.lon = lon;
    this.user = user;
  }
}
