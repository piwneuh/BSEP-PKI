package com.bsep.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CertificateNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="subjectSerialNumber")
    private String subjectSerialNumber;

    @Column(name="issuerSerialNumber")
    private String issuerSerialNumber;

    @Column(name="ca")
    private boolean ca;

    @Column(name="revoked")
    private boolean revoked;

    public CertificateNew(){

    }

    public CertificateNew(String subjectSerialNumber, String issuerSerialNumber, boolean ca){
        this.subjectSerialNumber = subjectSerialNumber;
        this.issuerSerialNumber = issuerSerialNumber;
        this.ca = ca;
        this.revoked = false;
    }


}
