package tqs.exdelivery.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table
@JsonSerialize
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private Long purchaseId;

  @Column private double lat;

  @Column private double lon;

  @Column private String state = "pending";

  @Column private String purchaseHost;

  @ManyToOne
  @JoinColumn(name = "deliveryCourierId", referencedColumnName = "id")
  private Courier courier;

  public Delivery(String purchaseHost, Long purchaseId, double lat, double lon) {
    this.purchaseHost = purchaseHost;
    this.purchaseId = purchaseId;
    this.lat = lat;
    this.lon = lon;
  }
}
