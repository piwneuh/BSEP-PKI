package com.bsep.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Revocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String serialNumber;

    @Column
    private String revocationReason;

    public Revocation() {
    }

    public Revocation(String serialNumber, String revocationReason) {
        this.serialNumber = serialNumber;
        this.revocationReason = revocationReason;
    }
}
