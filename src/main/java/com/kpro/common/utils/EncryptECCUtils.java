package com.kpro.common.utils;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class EncryptECCUtils {
    private static final Logger log = LoggerFactory.getLogger(EncryptECCUtils.class);

    public static void main(String[] args) throws InvalidAlgorithmParameterException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        String priKeyMe = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgkcupQuTPZMG1GPP+x1ETpw4LUDDvz0dpZWphT4BUrwCgCgYIKoZIzj0DAQehRANCAASDHB8x/3IfMwT6Mi9B/SNpBwx3N0aP19CL26viMJXt5Jzy4TcIIHKwJ0/XFRyukyRYPs9KPRbEMVWx+VsUnfZD";
        String pubKeyKPro = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEBPHT/6KflfVfCokCf2hnDJzyf5y8AaWDSgICtGAhVGjnQLWZIfKNmu48fkm5Pfgu+uT0rNR+TJ2SbDoPmTJXbg==";

        String priKeyKPro = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgXG/FsUcq9cnOr4wDE6viTn+nMeQi5qJUJA+HO0Oo2zqgCgYIKoZIzj0DAQehRANCAAQE8dP/op+V9V8KiQJ/aGcMnPJ/nLwBpYNKAgK0YCFUaOdAtZkh8o2a7jx+Sbk9+C765PSs1H5MnZJsOg+ZMldu";
        String pubKeyMe = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEgxwfMf9yHzME+jIvQf0jaQcMdzdGj9fQi9ur4jCV7eSc8uE3CCBysCdP1xUcrpMkWD7PSj0WxDFVsflbFJ32Qw==";

        String content_me = "call me lazy";
        String encodeContentMe = encode(priKeyMe, pubKeyKPro, content_me);
        System.out.println("----- Encrypt from me -----");
        System.out.println(encodeContentMe);
        String decodeContentMe = decode(priKeyKPro, pubKeyMe, encodeContentMe);
        System.out.println("----- Decode from KPro -----");
        System.out.println(decodeContentMe);
    }

    public static String decode(String priKey, String pubKey, String cipherContent) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        Cipher cipher = getCipher(priKey, pubKey, Cipher.DECRYPT_MODE);
        byte[] cipherContentBytes = Base64.getDecoder().decode(cipherContent);
        byte[] decryptedContentBytes = cipher.doFinal(cipherContentBytes);
        return new String(decryptedContentBytes, StandardCharsets.UTF_8);
    }

    public static String encode(String priKey, String pubKey, String content) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        Cipher cipher = getCipher(priKey, pubKey, Cipher.ENCRYPT_MODE);
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] cipherContent = cipher.doFinal(contentBytes);
        return Base64.getEncoder().encodeToString(cipherContent);
    }

    private static Cipher getCipher(String priKey, String pubKey, int mode) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        var provider = new BouncyCastleProvider();
        KeyAgreement ka = KeyAgreement.getInstance("ECDH", provider);
        ka.init(decodePrivateKey(priKey, "ECDH", provider));
        ka.doPhase(decodePublicKey(pubKey, "ECDH", provider), true);
        byte[] sharedSecret = ka.generateSecret();

        //(Optional) Hash the shared secret
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(sharedSecret);
        byte[] digest = messageDigest.digest();

        //(Optional) Split up hashed shared secret
        //Alternatively you can just use the shared key as session key and not use an iv
        int digestLen = digest.length;
        byte[] iv = Arrays.copyOfRange(digest, 0, (digestLen + 1)/2);
        byte[] sessionKey = Arrays.copyOfRange(digest, (digestLen + 1)/2, digestLen);

        SecretKey secretKey = new SecretKeySpec(sessionKey, 0, sessionKey.length, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", provider);
        cipher.init(mode, secretKey, ivSpec);
        return cipher;
    }

    private static Key decodePublicKey(String pubKey, String algorithm, BouncyCastleProvider provider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(pubKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
        return keyFactory.generatePublic(spec);
    }

    private static Key decodePrivateKey(String priKey, String algorithm, BouncyCastleProvider provider) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(priKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
        return keyFactory.generatePrivate(spec);
    }

    public static void genECKey() throws InvalidAlgorithmParameterException, IOException, NoSuchAlgorithmException {
        genECKey(null);
    }

    public static void genECKey(String path) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if (path == null) path = "src/main/resources/eec/";
        Provider provider = new BouncyCastleProvider();
        ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDH", provider);
        generator.initialize(spec, new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.concat("public_key_str")));
             BufferedWriter writer2 = new BufferedWriter(new FileWriter(path.concat("private_key_str")))){
            writer.write(publicKeyStr);
            writer2.write(privateKeyStr);
        }
    }
}
