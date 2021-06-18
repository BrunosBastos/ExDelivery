package tqs.exdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long purchaseId;

  private double lat;

  private double lon;

  @ColumnDefault("pending")
  private String state;

  private String host;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "courier_id", referencedColumnName = "id")
  private Courier courier;

  public Delivery(String host, Long purchaseId, double lat, double lon) {
      this.host = host;
      this.purchaseId = purchaseId;
      this.lat = lat;
      this.lon = lon;
  }
}
