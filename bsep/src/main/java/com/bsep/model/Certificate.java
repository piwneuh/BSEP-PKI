package com.bsep.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column
    protected int id;

    @ManyToOne
    @JoinColumn
    protected CertificateAuthority issuer;

    @OneToOne
    @JoinColumn
    @NotNull
    protected CertificateData certificateData;

    @OneToOne(mappedBy = "certificate")
    protected CertificateAuthority ca;

    @ManyToOne
    @JoinColumn
    protected User user;

    @Column
    @NotNull
    protected Date validFrom;

    @Column
    @NotNull
    protected Date validTo;

    @Column
    protected String cerFileName;

    public Certificate(CertificateAuthority issuer, CertificateData certificateData, CertificateAuthority ca, User user, Date validFrom, Date validTo, String cerFileName) {
        this.issuer = issuer;
        this.certificateData = certificateData;
        this.ca = ca;
        this.user = user;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.cerFileName = cerFileName;
    }

    public Certificate() {

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

    public CertificateData getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(CertificateData certificateData) {
        this.certificateData = certificateData;
    }

    public CertificateAuthority getCa() {
        return ca;
    }

    public void setCa(CertificateAuthority ca) {
        this.ca = ca;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getCerFileName() {
        return cerFileName;
    }

    public void setCerFileName(String cerFileName) {
        this.cerFileName = cerFileName;
    }
}
