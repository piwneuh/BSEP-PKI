package com.bsep.service;

import com.bsep.dto.CertificateBasicDTO;
import com.bsep.dto.CertificateDTO;
import com.bsep.dto.CertificateDetailsDTO;
import com.bsep.dto.IssuerDTO;
import com.bsep.model.CertType;
import com.bsep.model.CertificateNew;
import com.bsep.model.IssuerData;
import com.bsep.model.SubjectData;
import com.bsep.repository.CertificateRepository;
import com.bsep.repository.CertificatesWriter;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CertificateService {

    private KeyPair keyPairSubject = generateKeyPair();
    private X509Certificate cert;
    private X509Certificate issuerCertificate;
    private String fileLocationCA = "keystore/keystoreCA.jks";
    //private String fileLocationINT = "keystore/keystoreINT.jks";
    private String fileLocationEE = "keystore/keystoreEE.jks";
    private String passwordCA = "passwordCA";
    //private String passwordINT = "passwordINT";
    private String passwordEE = "passwordEE";


    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private CertificateGenerator certificateGenerator;
    @Autowired
    private CertificatesWriter certificatesWriter;
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
            IssuerData issuerData = certificatesWriter.findIssuerBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            if (checkValidityStatus(serialNumber)) {
                issuerCertificate = (X509Certificate) certificatesWriter.findCertificateBySerialNumber(serialNumber, fileLocationCA, passwordCA);
                cert = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO);
            }
            else
            {
                System.out.println("Issuer not valid");
                return false;
            }

        } else if (certificateDTO.getCertificateType().equals(CertType.REGULAR)) {
            String serialNumber = certificateDTO.getIssuerSerialNumber();
            IssuerData issuerData = certificatesWriter.findIssuerBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            if (checkValidityStatus(serialNumber)) {
                issuerCertificate = (X509Certificate) certificatesWriter.findCertificateBySerialNumber(serialNumber, fileLocationCA, passwordCA);
                cert = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO);
            }
            else
            {
                System.out.println("Issuer not valid");
                return false;
            }
        }

        if (cert == null) {
            return false;
        }

        if (certificateDTO.getCertificateType().equals(CertType.ROOT)) {
            keyExpirationService.save(cert);
            certificatesWriter.saveCertificate(new X509Certificate[]{cert}, keyPairSubject.getPrivate(), fileLocationCA, passwordCA);
            //sacuvaj u bazu
            CertificateNew certificateNew = new CertificateNew(cert.getSerialNumber().toString(),null,true);
            save(certificateNew);
            System.out.println("******** SAVED ROOT ********");
        }

        if (certificateDTO.getCertificateType().equals(CertType.INTERMEDIATE)) {
            Certificate[] issuerChain = certificatesWriter.findCertificateChainBySerialNumber(certificateDTO.getIssuerSerialNumber(), fileLocationCA, passwordCA);
            X509Certificate issuerChainX509[] = new X509Certificate[issuerChain.length + 1];
            issuerChainX509[0] = cert;
            for(int i=0;i<issuerChain.length;i++){
                issuerChainX509[i+1] = (X509Certificate) issuerChain[i];
            }

            //zapamti kada kljuc istekne za sertifikat
            keyExpirationService.save(issuerChainX509[0]);
            certificatesWriter.saveCertificate(issuerChainX509, keyPairSubject.getPrivate(), fileLocationCA, passwordCA);
            //sacuvaj u bazu
            CertificateNew certificateNew = new CertificateNew(cert.getSerialNumber().toString(),certificateDTO.getIssuerSerialNumber(),true);
            save(certificateNew);
            System.out.println("********SAVED INTERMEDIATE********");
        }

        if (certificateDTO.getCertificateType().equals(CertType.REGULAR)) {
            keyExpirationService.save(cert);
            certificatesWriter.saveCertificate(new X509Certificate[]{cert}, keyPairSubject.getPrivate(), fileLocationEE, passwordEE);
            //sacuvaj u bazu
            CertificateNew certificateNew = new CertificateNew(cert.getSerialNumber().toString(),certificateDTO.getIssuerSerialNumber(),false);
            save(certificateNew);
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

    public CertificateNew findCertificate(String serialNumber) {
        return certificateRepository.findBySubjectSerialNumber(serialNumber);
    }

    public void save(CertificateNew certDB) {
        this.certificateRepository.save(certDB);
    }

    public List<CertificateNew> findAllFirstChildren(String serialNumber) {
        return certificateRepository.findAllByIssuerSerialNumber(serialNumber);
    }

    public BigInteger generateRandom() {
        BigInteger maxLimit = new BigInteger("5000000000000");
        BigInteger minLimit = new BigInteger("25000000000");
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        Random randNum = new Random();
        int len = maxLimit.bitLength();
        return new BigInteger(len, randNum);
    }

    public CertificateDetailsDTO getCertificateDetails(String serialNumber) throws CertificateEncodingException, CertificateParsingException {
        CertificateNew certificateNew = findCertificateNew(serialNumber);
        X509Certificate cert;
        if(certificateNew.isCa()) {
            cert = (X509Certificate) certificatesWriter.findCertificateBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            if(cert != null) {
                JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cert);
                Certificate[] chain = certificatesWriter.findCertificateChainBySerialNumber(serialNumber, fileLocationCA, passwordCA);
                X509Certificate x509Cert;
                Boolean isRoot;
                if (chain.length == 1) { //ako je koren onda nema roditelja
                    x509Cert = (X509Certificate) chain[0];
                    isRoot = true;
                } else {
                    x509Cert = (X509Certificate) chain[1];
                    isRoot = false;
                }

                String issuerSerialNumber = x509Cert.getSerialNumber().toString();
                CertificateDetailsDTO cddto = new CertificateDetailsDTO(certHolder, cert, issuerSerialNumber, isRoot);
                return cddto;
            }else{
                return null;
            }

        }else{  //ako nije CA
            cert = (X509Certificate) certificatesWriter.findCertificateBySerialNumber(serialNumber, fileLocationEE, passwordEE);
            if(cert != null) {
                JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cert);
                String issuerSerialNumber = certificateNew.getIssuerSerialNumber();
                boolean isRoot = false;
                return new CertificateDetailsDTO(certHolder, cert, issuerSerialNumber, isRoot);
            }else{
                return null;
            }
        }
    }

    public CertificateNew findCertificateNew(String serialNumber) {
        return certificateRepository.findBySubjectSerialNumber(serialNumber);
    }

    public ArrayList<CertificateBasicDTO> getAllCertificates() throws CertificateEncodingException {
        //Svi CA se citaju
        Enumeration<String> aliases = certificatesWriter.getAllAliases(fileLocationCA, passwordCA);
        ArrayList<CertificateBasicDTO> certificateBasicDTOS = new ArrayList<>();

        //prolazi kroz elemente keystore-a
        while (aliases.hasMoreElements()) {
            Certificate c = certificatesWriter.findCertificateByAlias(aliases.nextElement(), fileLocationCA, passwordCA);
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) c);
            certificateBasicDTOS.add(new CertificateBasicDTO(certHolder));
        }

        //Svi end-entity se citaju
        aliases = certificatesWriter.getAllAliases(fileLocationEE, passwordEE);

        while (aliases.hasMoreElements()) {
            Certificate c = certificatesWriter.findCertificateByAlias(aliases.nextElement(), fileLocationEE, passwordEE);
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) c);
            certificateBasicDTOS.add(new CertificateBasicDTO(certHolder));
        }
        return certificateBasicDTOS;
    }

    public List<IssuerDTO> getAllCA() throws CertificateEncodingException {

        Enumeration<String> aliases = certificatesWriter.getAllAliases(fileLocationCA, passwordCA);
        List<IssuerDTO> issuerDTOS = new ArrayList<>();

        //prolazi kroz elemente keystore-a
        while (aliases.hasMoreElements()) {
            Certificate c = certificatesWriter.findCertificateByAlias(aliases.nextElement(), fileLocationCA, passwordCA);
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) c);
            if (((X509Certificate) c).getBasicConstraints() > -1) {
                if(checkValidityStatus(((X509Certificate) c).getSerialNumber().toString())){
                    issuerDTOS.add(new IssuerDTO(certHolder));
                }
            }
        }
        return issuerDTOS;
    }

    public boolean checkValidityStatus(String serialNumber) {

        ArrayList<Certificate> chain = new ArrayList<>();
        CertificateNew cDB = findCertificate(serialNumber);

        //ako u bazi nema sertifikata
        if(cDB == null){
            return false;
        }

        if(!cDB.isCa()){
            //ako je EE, dodajemo u niz i onda gledamo ceo lanac sa CA i dodajemo taj lanac u niz
            Certificate cert = certificatesWriter.findCertificateBySerialNumber(serialNumber, fileLocationEE,passwordEE);
            chain.add(cert);
            Certificate[] CAchain = certificatesWriter.findCertificateChainBySerialNumber(cDB.getIssuerSerialNumber(), fileLocationCA,passwordCA);
            for(Certificate c: CAchain) {
                chain.add(c);
            }

        }else{
            //ako je CA onda uzimamo lanac i dodajemo u niz
            Certificate[] CAchain = certificatesWriter.findCertificateChainBySerialNumber(serialNumber, fileLocationCA,passwordCA);
            for(Certificate c: CAchain) {
                chain.add(c);
            }
        }

        for(int i =0 ; i < chain.size(); i++) {

            X509Certificate x509Cert = (X509Certificate)chain.get(i);
            X509Certificate x509CACert =null;

            if(i != chain.size()-1) {
                x509CACert = (X509Certificate)chain.get(i+1);
            }else {
                x509CACert = (X509Certificate)chain.get(i); //na kraju proveri self-signed
            }

            //za svaki sertifikat u lancu da li je istekao
            try {
                x509Cert.checkValidity();
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                System.out.println("CERTIFICATE: "+x509Cert.getSerialNumber()+" EXPIRED.");
                e.printStackTrace();
                return false;
            }

            //provera potpisa
            try {
                x509Cert.verify(x509CACert.getPublicKey());
            } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
                    | SignatureException e) {
                System.out.println("CERTIFICATE: "+x509Cert.getSerialNumber()+" DOESN'T HAVE VALID SIGNATURE.");
                e.printStackTrace();
                return false;
            }

            //provera da li je revoked
            if(checkRevocationStatusOCSP(x509Cert.getSerialNumber().toString())) {
                System.out.println("CERTIFICATE: "+x509Cert.getSerialNumber()+" IS REVOKED.");
                return false;
            }

            //provera da li je issuer CA
            if(x509CACert.getBasicConstraints() == -1) {
                System.out.println("CERTIFICATE: "+x509CACert.getSerialNumber()+" IS NOT CA.");
                return false;
            }

            //provera da li je istekao
            if(keyExpirationService.expired(x509Cert.getSerialNumber().toString())) {
                System.out.println("CERTIFICATE'S: "+x509Cert.getSerialNumber()+" KEY EXPIRED.");
                return false;
            }

        }
        System.out.println("CERTIFICATE AND ITS CHAIN ARE VALID.");
        return true;
    }

    //provera da li je revokovan
    public boolean checkRevocationStatusOCSP(String serialNumber) {
        CertificateNew certNew = findCertificate(serialNumber);

        //ako nema sertifikata u bazi
        if(certNew == null){
            return true;
        }

        if(certNew.isRevoked()){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<CertificateBasicDTO> getAllByUsername(String username) throws CertificateEncodingException {

        //Svi CA se citaju
        Enumeration<String> alisases = certificatesWriter.getAllAliases(fileLocationCA, passwordCA);
        ArrayList<CertificateBasicDTO> certificateBasicDTOS = new ArrayList<>();

        //prolazi kroz elemente i vraca listu sertifikata koji imaju isti email unutar subject-a
        while (alisases.hasMoreElements()) {
            Certificate c = certificatesWriter.findCertificateByAlias(alisases.nextElement(), fileLocationCA, passwordCA);
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) c);
            RDN cn;
            String subjectEmail = "";
            if(certHolder.getSubject().getRDNs(BCStyle.E).length > 0) {
                cn = certHolder.getSubject().getRDNs(BCStyle.E)[0];
                subjectEmail = IETFUtils.valueToString(cn.getFirst().getValue());
            }
            if(subjectEmail.equals(username))
                certificateBasicDTOS.add(new CertificateBasicDTO(certHolder));
        }

        //Svi end-entity se citaju
        alisases = certificatesWriter.getAllAliases(fileLocationEE, passwordEE);

        while (alisases.hasMoreElements()) {
            Certificate c = certificatesWriter.findCertificateByAlias(alisases.nextElement(), fileLocationEE, passwordEE);
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) c);
            RDN cn;
            String subjectEmail = "";
            if(certHolder.getSubject().getRDNs(BCStyle.E).length > 0) {
                cn = certHolder.getSubject().getRDNs(BCStyle.E)[0];
                subjectEmail = IETFUtils.valueToString(cn.getFirst().getValue());
            }
            if(subjectEmail.equals(username))
                certificateBasicDTOS.add(new CertificateBasicDTO(certHolder));
        }
        return certificateBasicDTOS;
    }
}
