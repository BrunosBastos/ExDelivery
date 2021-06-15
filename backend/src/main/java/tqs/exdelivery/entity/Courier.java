package tqs.exdelivery.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
  private double reputation;

  @Column(nullable = false)
  private double lat;

  @Column(nullable = false)
  private double lon;

  @JsonIgnore
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;
}
