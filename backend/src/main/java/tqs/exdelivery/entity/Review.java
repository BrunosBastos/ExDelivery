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
@NoArgsConstructor
@AllArgsConstructor
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private int rating;

  @Column private String comment;

  @ManyToOne
  @JoinColumn(name = "reviewCourierId", referencedColumnName = "id")
  private Courier courier;

  @ManyToOne
  @JoinColumn(name = "reviewDeliveryId", referencedColumnName = "id")
  private Delivery delivery;

  public Review(int rating, String comment, Courier courier, Delivery delivery) {
    this.rating = rating;
    this.comment = comment;
    this.courier = courier;
    this.delivery = delivery;
  }
}
