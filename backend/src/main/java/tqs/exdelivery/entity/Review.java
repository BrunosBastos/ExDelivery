package tqs.exdelivery.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int rating;

  private String comment;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "courier_id", referencedColumnName = "id")
  private Courier courier;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "deliver_id", referencedColumnName = "id")
  private Delivery delivery;
}
