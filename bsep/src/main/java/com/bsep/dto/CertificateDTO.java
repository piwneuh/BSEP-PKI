package com.bsep.dto;

import com.bsep.model.CertType;
import com.bsep.model.UserRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> keyUsageList;
    private List<String> extendedKeyUsageList;
    private int typeSAN;
    private String valueSAN;
    boolean basicConstraints;


    public CertificateDTO() { }

    public CertificateDTO(String subjectCommonName, String subjectFirstName,
                          String subjectLastName, String subjectEmail, String subjectOrganization,
                          String subjectOrganizationUnit, String subjectState, String subjectCountry, String startDate, String endDate,
                          String issuerSerialNumber, CertType certificateType, int typeSAN, String valueSAN, boolean basicConstraints) {
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
        this.keyUsageList = new ArrayList<>();
        this.extendedKeyUsageList = new ArrayList<>();
        this.typeSAN = typeSAN;
        this.valueSAN = valueSAN;
        this.basicConstraints = basicConstraints;
    }
}
