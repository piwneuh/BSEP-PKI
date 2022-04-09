package com.bsep.service;

import com.bsep.model.CertType;
import com.bsep.model.Certificate;
import com.bsep.model.IssuerData;
import com.bsep.model.SubjectData;
import com.bsep.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;

@Service
public class CertificateService {

    @Autowired
    CertificateRepository repo;

    public void saveCertificateDB(SubjectData subjectData) {

        CertType cerType;
        if (subjectData.getType().equals("ROOT")) {
            cerType = CertType.ROOT;
        } else if (subjectData.getType().equals("INTERMEDIATE")) {
            cerType = CertType.INTERMEDIATE;
        } else {
            cerType = CertType.REGULAR;
        }

        if (subjectData.getStartDate().compareTo(subjectData.getEndDate()) < 0) {
            Certificate certificate = new Certificate();
            certificate.setSerialNumber(subjectData.getSerialNumber());
            certificate.setType(cerType);
            certificate.setValid(true);

            repo.save(certificate);
        }
    }

    public Iterable<Certificate> getAllValidCertificates() {
        return repo.findAllByValid(true);
    }

}
