package com.bsep.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class KeyExpiration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="serialNumber", nullable=false, unique = true)
    public String serialNumber;

    @Column(name="expirationDate", nullable=false)
    public Date expirationDate;

    public KeyExpiration() {
    }

    public KeyExpiration(String serialNumber, Date expirationDate) {
        this.serialNumber = serialNumber;
        this.expirationDate = expirationDate;
    }
}
