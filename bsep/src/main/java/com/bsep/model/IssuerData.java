package com.bsep.model;

import lombok.Data;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;


@Data
public class IssuerData {

    private X500Name x500name;
    private PrivateKey privateKey;

    public IssuerData(PrivateKey privateKey, X500Name x500name){}
}

