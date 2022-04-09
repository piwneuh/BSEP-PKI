package com.bsep.repository;

import com.bsep.model.Certificate;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepositoryImplementation<Certificate,String> {
    Iterable<Certificate> findAllByValid(boolean b);
}
