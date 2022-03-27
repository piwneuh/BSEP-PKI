package com.bsep.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class CertificateAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column
    protected int id;

    @ManyToOne
    @JoinColumn
    protected CertificateAuthority issuer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    @NotNull
    protected Certificate certificate;

    @Column
    protected String keyStoreFileName;

    @Column
    protected String keyStorePassword;

    @Column
    protected String keyStoreAlias;

    @Column
    protected String privateKeyPassword;

    @Column
    @NotNull
    protected CARole caRole;

    public CertificateAuthority(CertificateAuthority issuer, Certificate certificate, String keyStoreFileName, String keyStorePassword, String keyStoreAlias, String privateKeyPassword, CARole caRole) {
        this.issuer = issuer;
        this.certificate = certificate;
        this.keyStoreFileName = keyStoreFileName;
        this.keyStorePassword = keyStorePassword;
        this.keyStoreAlias = keyStoreAlias;
        this.privateKeyPassword = privateKeyPassword;
        this.caRole = caRole;
    }

    public CertificateAuthority() {

    }

    public int getId() {
        return id;
    }

    public CertificateAuthority getIssuer() {
        return issuer;
    }

    public void setIssuer(CertificateAuthority issuer) {
        this.issuer = issuer;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public String getKeyStoreFileName() {
        return keyStoreFileName;
    }

    public void setKeyStoreFileName(String keyStoreFileName) {
        this.keyStoreFileName = keyStoreFileName;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyStoreAlias() {
        return keyStoreAlias;
    }

    public void setKeyStoreAlias(String keyStoreAlias) {
        this.keyStoreAlias = keyStoreAlias;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }

    public void setPrivateKeyPassword(String privateKeyPassword) {
        this.privateKeyPassword = privateKeyPassword;
    }

    public CARole getCaRole() {
        return caRole;
    }

    public void setCaRole(CARole caRole) {
        this.caRole = caRole;
    }
}
