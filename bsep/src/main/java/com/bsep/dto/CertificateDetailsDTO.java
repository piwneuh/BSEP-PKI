package com.bsep.dto;

import lombok.Data;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class CertificateDetailsDTO {

    private String serialNumber;
    private int version;
    private String signatureAlgorithm;
    private String signatureHashAlgorithm;
    private String publicKey;
    private String startDate;
    private String endDate;
    private Boolean isRoot;

    private String subject;
    private String subjectGivenname;
    private String subjectSurname;
    private String subjectEmail;
    private String subjectCommonName;

    private String issuer;
    private String issuerGivenname;
    private String issuerSurname;
    private String issuerEmail;
    private String issuerCommonName;
    private String issuerSerialNumber;
    private String type;


    public CertificateDetailsDTO() {}

    public CertificateDetailsDTO(JcaX509CertificateHolder certificateHolder, X509Certificate cert, String issuerSerialNumber, Boolean isRoot) throws CertificateParsingException {
        this.serialNumber = certificateHolder.getSerialNumber().toString();
        this.version =certificateHolder.getVersionNumber();
        this.signatureAlgorithm = "sha256RSA";
        this.signatureHashAlgorithm = "sha256";
        this.publicKey = cert.getPublicKey().toString();
        this.isRoot = isRoot;

        String pattern = "dd/MMM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        this.startDate = df.format(certificateHolder.getNotBefore());
        this.endDate = df.format(certificateHolder.getNotAfter());

        X500Name subject = certificateHolder.getSubject();
        generateSubject(subject);

        X500Name issuer = certificateHolder.getIssuer();
        generateIssuer(issuer);
        this.issuerSerialNumber = issuerSerialNumber;

        if(cert.getBasicConstraints()>-1)
        {
            this.type = "CA";
        }
        else
        {
            this.type = "END-ENTITY";
        }
    }

    private void generateSubject(X500Name subject)
    {
        RDN cn;
        if(subject.getRDNs(BCStyle.E).length > 0) {
            cn = subject.getRDNs(BCStyle.E)[0];
            this.subjectEmail = IETFUtils.valueToString(cn.getFirst().getValue());
        }

        String temp;
        if(subject.getRDNs(BCStyle.CN).length > 0) {
            cn = subject.getRDNs(BCStyle.CN)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subjectCommonName = temp;
            this.subject = "CN=" + temp;
        }
        if(subject.getRDNs(BCStyle.O).length > 0) {
            cn = subject.getRDNs(BCStyle.O)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; O=" + temp;

        }
        if(subject.getRDNs(BCStyle.OU).length > 0) {
            cn = subject.getRDNs(BCStyle.OU)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; OU=" + temp;
        }
        if(subject.getRDNs(BCStyle.ST).length > 0) {
            cn = subject.getRDNs(BCStyle.ST)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; ST=" + temp;
        }
        if(subject.getRDNs(BCStyle.C).length > 0) {
            cn = subject.getRDNs(BCStyle.C)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; C=" + temp;
        }
        if(subject.getRDNs(BCStyle.GIVENNAME).length > 0) {
            cn = subject.getRDNs(BCStyle.GIVENNAME)[0];
            this.subjectGivenname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.SURNAME).length > 0) {
            cn = subject.getRDNs(BCStyle.SURNAME)[0];
            this.subjectSurname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
    }

    private void generateIssuer(X500Name issuer)
    {
        RDN cn;
        if(issuer.getRDNs(BCStyle.E).length > 0) {
            cn = issuer.getRDNs(BCStyle.E)[0];
            this.issuerEmail = IETFUtils.valueToString(cn.getFirst().getValue());
        }

        String temp;
        if(issuer.getRDNs(BCStyle.CN).length > 0) {
            cn = issuer.getRDNs(BCStyle.CN)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuerCommonName = temp;
            this.issuer = "CN=" + temp;
        }
        if(issuer.getRDNs(BCStyle.O).length > 0) {
            cn = issuer.getRDNs(BCStyle.O)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; O=" + temp;
        }
        if(issuer.getRDNs(BCStyle.OU).length > 0) {
            cn = issuer.getRDNs(BCStyle.OU)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; OU=" + temp;
        }
        if(issuer.getRDNs(BCStyle.ST).length > 0) {
            cn = issuer.getRDNs(BCStyle.ST)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; ST=" + temp;
        }
        if(issuer.getRDNs(BCStyle.C).length > 0) {
            cn = issuer.getRDNs(BCStyle.C)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; C=" + temp;
        }
        if(issuer.getRDNs(BCStyle.GIVENNAME).length > 0) {
            cn = issuer.getRDNs(BCStyle.GIVENNAME)[0];
            this.issuerGivenname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(issuer.getRDNs(BCStyle.SURNAME).length > 0) {
            cn = issuer.getRDNs(BCStyle.SURNAME)[0];
            this.issuerSurname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
    }
}
