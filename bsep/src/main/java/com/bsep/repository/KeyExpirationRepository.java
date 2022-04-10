package com.bsep.repository;

import com.bsep.model.KeyExpiration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyExpirationRepository extends JpaRepository<KeyExpiration,Long> {
    KeyExpiration findBySerialNumber(String serialNumber);
}
