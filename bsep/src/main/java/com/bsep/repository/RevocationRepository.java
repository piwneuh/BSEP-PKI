package com.bsep.repository;

import com.bsep.model.Revocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface RevocationRepository extends JpaRepositoryImplementation<Revocation, Long> {
    Revocation findBySerialNumber(String toString);
}
