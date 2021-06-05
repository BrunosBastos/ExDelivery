package tqs.exdelivery.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import tqs.exdelivery.entity.Courier;

import javax.persistence.*;

@Entity
@Data
@JsonSerialize
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column private String email;

    @Column private String password;

    @Column private boolean isSuperUser;

    @OneToOne(mappedBy = "user")
    private Courier courier;

}
