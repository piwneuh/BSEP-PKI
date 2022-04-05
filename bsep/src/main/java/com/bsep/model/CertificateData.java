package com.bsep.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class CertificateData {

    private Long id;

    private BigInteger serialNumber;

    private PublicKey publicKey;

    private X500Name x500Name;

    private Date validFrom;

    private Date validUntil;

    private boolean withdrawn;



    public CertificateData() { }

    public CertificateData(BigInteger serialNumber, PublicKey publicKey, X500Name x500Name, Date validFrom, Date validUntil){
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.x500Name = x500Name;
        this.validFrom=validFrom;
        this.validUntil=validUntil;
        this.withdrawn=false;


    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }


    public X500Name getX500Name() {
        return this.x500Name;
    }

    public void setX500Name(X500Name X500Name) {
        this.x500Name = X500Name;
    }



    public Date getValidFrom() {
        return this.validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return this.validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }



    public boolean isWithdrawn() {
        return this.withdrawn;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }
}
