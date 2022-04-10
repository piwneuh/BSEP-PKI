package com.bsep.service;

import com.bsep.model.KeyExpiration;
import com.bsep.repository.KeyExpirationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

@Service
public class KeyExpirationService {

    @Autowired
    private KeyExpirationRepository keyExpirationRepository;

    public boolean expired(String serialNumber){
        KeyExpiration keyExpiration = keyExpirationRepository.findBySerialNumber(serialNumber);

        if(keyExpiration == null){
            return true;
        }

        Date date = new Date(System.currentTimeMillis());

        if(keyExpiration.getExpirationDate().before(date))
            return true;

        return false;
    }

    public KeyExpiration save(X509Certificate certificate){
        Date date = certificate.getNotBefore();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, 2);
        date = c.getTime();
        return keyExpirationRepository.save(new KeyExpiration(certificate.getSerialNumber().toString(),date));
    }

}
