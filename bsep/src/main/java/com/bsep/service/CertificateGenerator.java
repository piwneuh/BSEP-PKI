package com.bsep.service;

import com.bsep.dto.CertificateDTO;
import com.bsep.model.CertType;
import com.bsep.model.IssuerData;
import com.bsep.model.SubjectData;
import com.bsep.repository.CertificatesWriter;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class CertificateGenerator {

    @Autowired
    private CertificatesWriter store;

    private String fileLocationCA = "keystore/keystoreCA.jks";
    private String passwordCA = "passwordCA";

    public CertificateGenerator() {}

    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, CertificateDTO certDTO) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            //Adding BasicConstraints for all
            certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(certDTO.isBasicConstraints()));

            //Adding KeyUsage for all
            int allKeyUsages = 0;
            for (Integer i : certDTO.getKeyUsageList()) {
                allKeyUsages = allKeyUsages | i;
            }
            certGen.addExtension(Extension.keyUsage, true, new KeyUsage(allKeyUsages));

            //Adding ExtendedKeyUsage only if it exists, if it is End Entity
            if(certDTO.getExtendedKeyUsageList() != null) {
                KeyPurposeId kpi[] = new KeyPurposeId[certDTO.getExtendedKeyUsageList().size()];
                int i = 0;
                for (String s : certDTO.getExtendedKeyUsageList()) {
                    ASN1ObjectIdentifier newKeyPurposeIdOID = new ASN1ObjectIdentifier(s);
                    KeyPurposeId newKeyPurposeId = KeyPurposeId.getInstance(newKeyPurposeIdOID);
                    kpi[i] = newKeyPurposeId;
                    i++;

                }
                ExtendedKeyUsage eku = new ExtendedKeyUsage(kpi);
                certGen.addExtension(Extension.extendedKeyUsage, false, eku);
            }

            //Adding Subject Alternative Name only if it exists, it is not required
            if(certDTO.getValueSAN() != null){
                GeneralName altName = new GeneralName(certDTO.getTypeSAN(), certDTO.getValueSAN());
                GeneralNames subjectAltName = new GeneralNames(altName);
                System.out.println("Alt name: "+subjectAltName.getNames());
                certGen.addExtension(Extension.subjectAlternativeName,false,subjectAltName);
            }

            //Adding Subject Key Identifier for all
            JcaX509ExtensionUtils utils = new JcaX509ExtensionUtils();

            SubjectKeyIdentifier ski = utils.createSubjectKeyIdentifier(subjectData.getPublicKey());
            certGen.addExtension(Extension.subjectKeyIdentifier, false, ski);

            //Adding Authority Key Identifier if it is not root
            if(certDTO.getCertificateType() != CertType.ROOT) {
                java.security.cert.Certificate certIssuer =  store.findCertificateBySerialNumber(certDTO.getIssuerSerialNumber(), fileLocationCA,passwordCA);
                AuthorityKeyIdentifier aki = utils.createAuthorityKeyIdentifier(certIssuer.getPublicKey());
                certGen.addExtension(Extension.authorityKeyIdentifier, false, aki);
            }


            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            return certConverter.getCertificate(certHolder);

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
