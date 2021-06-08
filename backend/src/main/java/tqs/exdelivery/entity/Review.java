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

    public Review(long id, int rating, String comment) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
    }

    public Review(long id, int rating, String comment, Courier courier, Delivery delivery) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.courier = courier;
        this.delivery = delivery;
    }

}
