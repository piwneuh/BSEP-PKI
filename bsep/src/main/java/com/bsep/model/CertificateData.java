package com.bsep.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class CertificateData {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column
    protected int id;

    @Column
    @NotNull
    protected int serialNumber;

    @Column(length = 2048)
    @NotNull
    protected String publicKey;

    @Column
    @NotNull
    protected String commonName;

    @Column
    protected String organization;

    @Column
    protected String organizationalUnit;

    @Column
    protected String country;

    @Column
    protected String emailAddress;

    public CertificateData(int serialNumber, String publicKey, String commonName, String organization, String organizationalUnit, String country, String emailAddress) {
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.commonName = commonName;
        this.organization = organization;
        this.organizationalUnit = organizationalUnit;
        this.country = country;
        this.emailAddress = emailAddress;
    }


    public CertificateData() {

    }

    public int getId() {
        return id;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(String organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
