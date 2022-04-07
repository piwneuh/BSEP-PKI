package com.bsep.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;


public class Certificate {

    private Long id;

    private CertificateData certificateData;

    public Certificate() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CertificateData getCertificateData() {
        return this.certificateData;
    }

    public void setCertificateData(CertificateData certificateData) {
        this.certificateData = certificateData;
    }
}
