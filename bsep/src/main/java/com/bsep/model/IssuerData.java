package com.bsep.model;

import lombok.Data;

import java.security.PrivateKey;


@Data
public class IssuerData {

    private String x500name;
    private PrivateKey privateKey;
}