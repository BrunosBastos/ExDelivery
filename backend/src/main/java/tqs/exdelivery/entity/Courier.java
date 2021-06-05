package tqs.exdelivery.entity;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import tqs.exdelivery.entity.User;

@Entity
@Data
@JsonSerialize
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courierId;

    @Column
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}
