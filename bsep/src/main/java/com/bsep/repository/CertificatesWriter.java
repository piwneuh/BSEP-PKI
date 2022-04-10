package com.bsep.repository;

import com.bsep.model.IssuerData;
import com.bsep.service.KeyStoreService;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


@Repository
public class CertificatesWriter {

    @Autowired
    private KeyStoreService keyStoreService;

    public void saveCertificate(X509Certificate[] chain, PrivateKey privateKey, String fileLocation, String password) throws CertificateEncodingException {
        String serialNumber = chain[0].getSerialNumber().toString();
        keyStoreService.loadKeyStore(fileLocation, password.toCharArray());
        keyStoreService.write(serialNumber, privateKey, serialNumber.toCharArray(), chain);
        keyStoreService.saveKeyStore(fileLocation, password.toCharArray());

        Enumeration<String> aliases = this.getAllAliases(fileLocation, password);
        System.out.println("Aliases in keystore" + fileLocation + ":");
        while(aliases.hasMoreElements()){
            System.out.println(aliases.nextElement());
        }

        Certificate[] certificateChain = this.keyStoreService.readCertificateChain(fileLocation,password,serialNumber);
        System.out.println("Chain length: " + certificateChain.length); //ispis duzine i elemenata lanca(common name)
        System.out.println("Chain:");
        for(int i=0;i<certificateChain.length;i++){
            System.out.println(new JcaX509CertificateHolder((X509Certificate) certificateChain[i]).getSerialNumber());
            X500Name x500name = new JcaX509CertificateHolder((X509Certificate) certificateChain[i]).getSubject();
            RDN cn = x500name.getRDNs(BCStyle.CN)[0];
            System.out.println(IETFUtils.valueToString(cn.getFirst().getValue()));
        }
    }

    public IssuerData findIssuerBySerialNumber(String serialNumber, String fileLocation, String password) {
        return keyStoreService.readIssuerFromStore(fileLocation, serialNumber,
                password.toCharArray(), serialNumber.toCharArray());
    }

    public Certificate findCertificateBySerialNumber(String serialNumber, String fileLocation, String password) {
        return keyStoreService.readCertificate(fileLocation, password, serialNumber);
    }

    public Certificate findCertificateByAlias(String alias, String fileLocation, String password){
        return keyStoreService.readCertificate(fileLocation, password, alias);
    }

    public Certificate[] findCertificateChainBySerialNumber(String serialNumber, String fileLocation, String password) {
        return keyStoreService.readCertificateChain(fileLocation, password, serialNumber);
    }

    public Enumeration<String> getAllAliases(String keyStoreFile, String password) {
        return keyStoreService.getAllAliases(keyStoreFile, password);
    }
}
