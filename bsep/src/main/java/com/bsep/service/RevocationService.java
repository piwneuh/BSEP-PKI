package com.bsep.service;

import com.bsep.dto.RevocationDTO;
import com.bsep.model.CertificateNew;
import com.bsep.model.Revocation;
import com.bsep.repository.CertificatesWriter;
import com.bsep.repository.RevocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

@Service
public class RevocationService {

    @Autowired
    private RevocationRepository revocationRepository;

    @Autowired
    private CertificatesWriter store;

    @Autowired
    private CertificateService certificateService;

    private String fileLocationCA = "keystore/keystoreCA.jks";
    private String fileLocationEE = "keystore/keystoreEE.jks";
    private String passwordCA = "passwordCA";
    private String passwordEE = "passwordEE";

    public boolean revokeCertificate(RevocationDTO revocationDTO) {

        boolean revoked = this.checkRevocationStatusOCSP(revocationDTO.getSerialNumber());
        if(revoked){
            return true;
        }

        Revocation revocation = new Revocation(revocationDTO.getSerialNumber(), revocationDTO.getRevocationReason());
        revocationRepository.save(revocation);
        return false;

    }

    public boolean checkRevocationStatusOCSP(String serialNumber) {

        CertificateNew certDB = certificateService.findCertificate(serialNumber);
        X509Certificate certificateChainX509[];

        if(certDB == null){
            return true;
        }

        if(certDB.isCa()) {
            Certificate certificateChain[] = store.findCertificateChainBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            System.out.println(certificateChain.length);
            certificateChainX509 = new X509Certificate[certificateChain.length];
            for (int i = 0; i < certificateChain.length; i++) {
                certificateChainX509[i] = (X509Certificate) certificateChain[i];
            }
        }else {
            Certificate cert = store.findCertificateBySerialNumber(serialNumber,fileLocationEE,passwordEE);
            Certificate issuerChain[] = store.findCertificateChainBySerialNumber(certDB.getIssuerSerialNumber(),fileLocationCA,passwordCA);
            certificateChainX509 = new X509Certificate[issuerChain.length + 1];
            certificateChainX509[0] = (X509Certificate) cert;
            for(int i=0;i<issuerChain.length;i++){
                certificateChainX509[i+1] = (X509Certificate)issuerChain[i];
            }
        }

        //provere se svi u lancu, cim se naidje na neki koji je povucen odmah se vraca true
        for(int i=0;i<certificateChainX509.length;i++){
            System.out.println(certificateChainX509[i].getSerialNumber().toString());
            Revocation rc = revocationRepository.findBySerialNumber(certificateChainX509[i].getSerialNumber().toString());
            if(rc != null){
                return true;
            }
        }

        return false;
    }
}
