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
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long purchaseId;

    private String state;

    private String host;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "courier_id", referencedColumnName = "id")
    private Courier courier;
}
