package com.bsep.model;

import lombok.Data;

import java.security.PublicKey;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;

@Data
public class SubjectData {

    private PublicKey publicKey;
    private X500Name x500name;
    private String serialNumber;
    private CertType type;
    private Date startDate;
    private Date endDate;

}
