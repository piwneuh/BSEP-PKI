package com.bsep.controller;

import com.bsep.dto.CertificateBasicDTO;
import com.bsep.dto.CertificateDTO;
import com.bsep.dto.CertificateDetailsDTO;
import com.bsep.model.SubjectData;
import com.bsep.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/api")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping(value="/addNewCertificate", consumes="application/json")
    public ResponseEntity<?> addNewCertificate(@RequestBody CertificateDTO certificateDTO) throws CertificateEncodingException {

        boolean certCreated = certificateService.addNewCertificate(certificateDTO);
        if(certCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(value="/getCertificateDetails/{serialNumber}", produces = "application/json")
    public ResponseEntity<?> getCertificateDetails(@PathVariable("serialNumber") String serialNumber) throws CertificateEncodingException, CertificateParsingException {
        return ResponseEntity.ok(certificateService.getCertificateDetails(serialNumber));
    }

    @GetMapping(value="/getAllCertificates", produces = "application/json")
    public ResponseEntity<?> getAllCertificates() throws CertificateEncodingException, CertificateParsingException {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @GetMapping(value="/getAllCA", produces = "application/json")
    public ResponseEntity<?> getAllCA() throws CertificateEncodingException, CertificateParsingException {
        return ResponseEntity.ok(certificateService.getAllCA());
    }

    @GetMapping(value="/getAllByUsername/{username}", produces = "application/json")
    public ResponseEntity<?> getAllByUsername(@PathVariable("username") String username) throws CertificateEncodingException, CertificateParsingException {
        return ResponseEntity.ok(certificateService.getAllByUsername(username));
    }

    @GetMapping(value="/checkValidityStatus/{serialNumber}", produces = "application/json")
    public ResponseEntity<?> checkValidityStatus(@PathVariable("serialNumber") String serialNumber) throws CertificateEncodingException, CertificateParsingException {
        return ResponseEntity.ok(certificateService.checkValidityStatus(serialNumber));
    }
}