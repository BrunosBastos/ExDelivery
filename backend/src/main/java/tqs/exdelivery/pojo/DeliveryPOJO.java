package tqs.exdelivery.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPOJO {
  private String purchaseHost;
  private Long purchaseId;
  private double lat;
  private double lon;
}
