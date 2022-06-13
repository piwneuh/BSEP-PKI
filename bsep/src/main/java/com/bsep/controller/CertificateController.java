package com.bsep.controller;

import com.bsep.dto.CertificateBasicDTO;
import com.bsep.dto.CertificateDTO;
import com.bsep.dto.CertificateDetailsDTO;
import com.bsep.dto.RevocationDTO;
import com.bsep.model.SubjectData;
import com.bsep.service.CertificateService;
import com.bsep.service.RevocationService;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Origin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/api")
@CrossOrigin(origins = "http://localhost:4200/")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private RevocationService revocationService;

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
    
    @PostMapping(value="/revokeCertificate", consumes="application/json")
    public ResponseEntity<?> revokeCertificate(@RequestBody RevocationDTO revocationDTO){
        return ResponseEntity.ok(revocationService.revokeCertificate(revocationDTO));
    }

    @PostMapping("/downloadCertificate")
    public ResponseEntity<Resource> downloadCertificate(@RequestBody CertificateDTO certificateDTO)
            throws CertificateException, IOException {
        certificateService.extractCertificate(certificateDTO);
        File file = new File(certificateDTO.getSubjectCommonName() + ".crt");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        file.deleteOnExit();

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}