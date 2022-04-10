package com.bsep.service;

import com.bsep.dto.CertificateDTO;
import com.bsep.model.CertType;
import com.bsep.model.CertificateNew;
import com.bsep.model.IssuerData;
import com.bsep.model.SubjectData;
import com.bsep.repository.CertificateRepository;
import com.bsep.repository.CertificatesWriter;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class CertificateService {

    private KeyPair keyPairSubject = generateKeyPair();
    private X509Certificate cert;
    private X509Certificate issuerCertificate;
    private String fileLocationCA = "keystore/keystoreCA.jks";
    private String fileLocationINT = "keystore/keystoreINT.jks";
    private String fileLocationEE = "keystore/keystoreEE.jks";
    private String passwordCA = "passwordCA";
    private String passwordINT = "passwordINT";
    private String passwordEE = "passwordEE";


    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private CertificateGenerator certificateGenerator;
    @Autowired
    private CertificatesWriter store;
    @Autowired
    private KeyExpirationService keyExpirationService;

    public boolean addNewCertificate(CertificateDTO certificateDTO) throws CertificateEncodingException {
        keyPairSubject = generateKeyPair();
        SubjectData subjectData = generateSubjectData(certificateDTO);

        if (certificateDTO.getCertificateType().equals(CertType.ROOT)) {
            issuerCertificate = null;
            IssuerData issuerData = generateIssuerData(certificateDTO);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO);

        } else if (certificateDTO.getCertificateType().equals(CertType.INTERMEDIATE)) {

            String serialNumber = certificateDTO.getIssuerSerialNumber();
            IssuerData issuerData = store.findIssuerBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            issuerCertificate = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO);

        } else if (certificateDTO.getCertificateType().equals(CertType.REGULAR)) {

            String serialNumber = certificateDTO.getIssuerSerialNumber();
            IssuerData issuerData = store.findIssuerBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            issuerCertificate = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData,
                    certificateDTO);
        }

        if (cert == null) {
            return false;
        }

        if (certificateDTO.getCertificateType().equals(CertType.ROOT)) {
            keyExpirationService.save(cert);
            store.saveCertificate(new X509Certificate[]{cert}, keyPairSubject.getPrivate(), fileLocationCA, passwordCA);
            //save in the datebase
            CertificateNew certDB = new CertificateNew(cert.getSerialNumber().toString(),null,true);
            save(certDB);
            System.out.println("******** SAVED ROOT ********");
        }

        if (certificateDTO.getCertificateType().equals(CertType.INTERMEDIATE)) {
            Certificate[] issuerChain = store.findCertificateChainBySerialNumber(certificateDTO.getIssuerSerialNumber(), fileLocationCA, passwordCA);
            X509Certificate issuerChainX509[] = new X509Certificate[issuerChain.length + 1];
            issuerChainX509[0] = cert;
            for(int i=0;i<issuerChain.length;i++){
                issuerChainX509[i+1] = (X509Certificate) issuerChain[i];
            }

            //  remember when the key expires for the certificate
            keyExpirationService.save(issuerChainX509[0]);

            store.saveCertificate(issuerChainX509, keyPairSubject.getPrivate(), fileLocationCA, passwordCA);
            //save in the database
            CertificateNew certDB = new CertificateNew(cert.getSerialNumber().toString(),certificateDTO.getIssuerSerialNumber(),true);
            save(certDB);
            System.out.println("********SAVED INTERMEDIATE********");
        }

        if (certificateDTO.getCertificateType().equals(CertType.REGULAR)) {
            keyExpirationService.save(cert);
            store.saveCertificate(new X509Certificate[]{cert}, keyPairSubject.getPrivate(), fileLocationEE, passwordEE);
            //save in the database
            CertificateNew certDB = new CertificateNew(cert.getSerialNumber().toString(),certificateDTO.getIssuerSerialNumber(),false);
            save(certDB);
            System.out.println("******** SAVED END-ENTITY ********");

        }

        return true;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SubjectData generateSubjectData(CertificateDTO certificateDTO) {
        try {

            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(certificateDTO.getStartDate());
            Date endDate = iso8601Formater.parse(certificateDTO.getEndDate());

            String serialNumber = generateRandom().toString();

            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            if(!certificateDTO.getSubjectCommonName().equals("")) {
                builder.addRDN(BCStyle.CN, certificateDTO.getSubjectCommonName());
            }
            if(!certificateDTO.getSubjectLastName().equals("")) {
                builder.addRDN(BCStyle.SURNAME, certificateDTO.getSubjectLastName());
            }
            if(!certificateDTO.getSubjectFirstName().equals("")) {
                builder.addRDN(BCStyle.GIVENNAME, certificateDTO.getSubjectFirstName());
            }
            if(!certificateDTO.getSubjectOrganization().equals("")) {
                builder.addRDN(BCStyle.O, certificateDTO.getSubjectOrganization());
            }
            if(!certificateDTO.getSubjectOrganizationUnit().equals("")) {
                builder.addRDN(BCStyle.OU, certificateDTO.getSubjectOrganizationUnit());
            }
            if(!certificateDTO.getSubjectState().equals("")){
                builder.addRDN(BCStyle.ST, certificateDTO.getSubjectState());
            }
            if(!certificateDTO.getSubjectCountry().equals("")) {
                builder.addRDN(BCStyle.C, certificateDTO.getSubjectCountry());
            }
            if(!certificateDTO.getSubjectEmail().equals("")) {
                builder.addRDN(BCStyle.E, certificateDTO.getSubjectEmail());
            }

            return new SubjectData(keyPairSubject.getPublic(), builder.build(), serialNumber, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private IssuerData generateIssuerData(CertificateDTO certificateDTO) {
        try {
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            if(!certificateDTO.getSubjectCommonName().equals("")) {
                builder.addRDN(BCStyle.CN, certificateDTO.getSubjectCommonName());
            }
            if(!certificateDTO.getSubjectLastName().equals("")) {
                builder.addRDN(BCStyle.SURNAME, certificateDTO.getSubjectLastName());
            }
            if(!certificateDTO.getSubjectFirstName().equals("")) {
                builder.addRDN(BCStyle.GIVENNAME, certificateDTO.getSubjectFirstName());
            }
            if(!certificateDTO.getSubjectOrganization().equals("")) {
                builder.addRDN(BCStyle.O, certificateDTO.getSubjectOrganization());
            }
            if(!certificateDTO.getSubjectOrganizationUnit().equals("")) {
                builder.addRDN(BCStyle.OU, certificateDTO.getSubjectOrganizationUnit());
            }
            if(!certificateDTO.getSubjectState().equals("")){
                builder.addRDN(BCStyle.ST, certificateDTO.getSubjectState());
            }
            if(!certificateDTO.getSubjectCountry().equals("")) {
                builder.addRDN(BCStyle.C, certificateDTO.getSubjectCountry());
            }
            if(!certificateDTO.getSubjectEmail().equals("")) {
                builder.addRDN(BCStyle.E, certificateDTO.getSubjectEmail());
            }

            return new IssuerData(builder.build(), keyPairSubject.getPrivate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //####
    public CertificateNew findCertificate(String serialNumber) {
        return certificateRepository.findBySubjectSerialNumber(serialNumber);
    }

    public void save(CertificateNew certDB) {
        this.certificateRepository.save(certDB);
    }

    public List<CertificateNew> findAllFirstChildren(String serialNumber) {
        return certificateRepository.findAllByIssuerSerialNumber(serialNumber);
    }
    //######

    public BigInteger generateRandom() {
        BigInteger maxLimit = new BigInteger("5000000000000");
        BigInteger minLimit = new BigInteger("25000000000");
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        Random randNum = new Random();
        int len = maxLimit.bitLength();
        return new BigInteger(len, randNum);
    }
}
