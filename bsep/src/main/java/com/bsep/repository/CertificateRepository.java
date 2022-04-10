package com.bsep.repository;

import com.bsep.model.CertificateNew;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepositoryImplementation<CertificateNew,String> {
    CertificateNew findBySubjectSerialNumber(String serialNumber);
    List<CertificateNew> findAllByIssuerSerialNumber(String serialNumber);
}
