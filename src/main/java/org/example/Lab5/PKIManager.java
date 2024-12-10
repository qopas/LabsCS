package org.example.Lab5;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class PKIManager {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Generate an RSA Key Pair
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        return keyGen.generateKeyPair();
    }

    // Create a self-signed certificate
    public static X509Certificate createSelfSignedCertificate(KeyPair keyPair, String dn, int validityDays) throws Exception {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + validityDays * 24L * 60L * 60L * 1000L);

        X500Name issuer = new X500Name(dn);
        X500Name subject = issuer; // For self-signed certificates, issuer = subject
        BigInteger serialNumber = BigInteger.valueOf(now);

        // Build the certificate
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer, serialNumber, startDate, endDate, subject, keyPair.getPublic()
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
    }

    // Sign data using a private key
    public static byte[] signData(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    // Verify signature using a public key
    public static boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureBytes);
    }

    public static void main(String[] args) throws Exception {
        // Generate CA key pair
        KeyPair caKeyPair = generateKeyPair(4096);

        // Create self-signed CA certificate
        X509Certificate caCertificate = createSelfSignedCertificate(caKeyPair, "CN=MyCA, O=MyOrg, C=US", 3650);
        System.out.println("CA Certificate Generated:\n" + caCertificate);

        // Generate User key pair
        KeyPair userKeyPair = generateKeyPair(2048);

        // Create user certificate (self-signed for simplicity; normally signed by CA)
        X509Certificate userCertificate = createSelfSignedCertificate(userKeyPair, "CN=User1, O=MyOrg, C=US", 365);
        System.out.println("User Certificate Generated:\n" + userCertificate);

        // Example: Signing and verifying data
        String originalData = "This is the original message.";
        byte[] dataBytes = originalData.getBytes();

        // Sign the data
        byte[] signature = signData(dataBytes, userKeyPair.getPrivate());
        System.out.println("Original Data: " + originalData);
        System.out.println("Signature created.");

        // Verify the original signature
        boolean isValidOriginal = verifySignature(dataBytes, signature, userKeyPair.getPublic());
        System.out.println("Verification of original data: " + (isValidOriginal ? "SUCCESS" : "FAILURE"));

        // Modify the data
        String modifiedData = "This is the tampered message.";
        byte[] tamperedBytes = modifiedData.getBytes();
        System.out.println("\nModified Data: " + modifiedData);

        // Try verifying the tampered data with the original signature
        boolean isValidTampered = verifySignature(tamperedBytes, signature, userKeyPair.getPublic());
        System.out.println("Verification of tampered data: " + (isValidTampered ? "SUCCESS" : "FAILURE"));
    }
}
