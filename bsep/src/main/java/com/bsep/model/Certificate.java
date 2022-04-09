package com.bsep.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
public class Certificate {

    @Id
    private String serialNumber;

    @Column
    private CertType type;

    @Column
    private boolean valid;
}
