package com.bsep.dto;

import com.bsep.model.CertType;
import lombok.Data;

@Data
public class CertificateDTO {

    private String subjectCommonName;
    private String subjectFirstName;
    private String subjectLastName;
    private String subjectEmail;
    private String subjectOrganization;
    private String subjectOrganizationUnit;
    private String subjectState;
    private String subjectCountry;
    private String startDate;
    private String endDate;
    private String issuerSerialNumber;
    private CertType certificateType;

    public CertificateDTO() { }

    public CertificateDTO(String subjectCommonName, String subjectFirstName,
                          String subjectLastName, String subjectEmail, String subjectOrganization,
                          String subjectOrganizationUnit, String subjectState, String subjectCountry, String startDate, String endDate,
                          String issuerSerialNumber, CertType certificateType) {
        this.subjectCommonName = subjectCommonName;
        this.subjectFirstName = subjectFirstName;
        this.subjectLastName = subjectLastName;
        this.subjectEmail = subjectEmail;
        this.subjectOrganization = subjectOrganization;
        this.subjectOrganizationUnit = subjectOrganizationUnit;
        this.subjectState = subjectState;
        this.subjectCountry = subjectCountry;
        this.startDate = startDate;
        this.endDate = endDate;
        this.issuerSerialNumber = issuerSerialNumber;
        this.certificateType = certificateType;
    }
}
