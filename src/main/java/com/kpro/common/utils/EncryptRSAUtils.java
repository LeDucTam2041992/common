package com.kpro.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptRSAUtils {
    private static final Logger log = LoggerFactory.getLogger(EncryptRSAUtils.class);
    private static final String DEFAULT_DIR_PATH = "src/main/resources/";
    private static final String RSA_ALGORITHM = "RSA";
    public static String encryptRSA(String en) {
        String strEncrypt = null;
        try {
            byte[] b = EncryptRSAUtils.class.getClassLoader()
                    .getResourceAsStream("publicKey.rsa").readAllBytes();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = factory.generatePublic(spec);
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptOut = c.doFinal(en.getBytes());
            strEncrypt = Base64.getEncoder().encodeToString(encryptOut);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strEncrypt;
    }

    public static String decryptRSA(String de) {
        String strDecrypt = null;
        try {

            byte[] b = EncryptRSAUtils.class.getClassLoader()
                    .getResourceAsStream("privateKey.rsa").readAllBytes();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = factory.generatePrivate(spec);

            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, priKey);

            byte[] decryptOut = c.doFinal(Base64.getDecoder().decode(
                    de));
            strDecrypt = new String(decryptOut);

        } catch (Exception ex) {
            log.error("{} decrypt error {}", EncryptRSAUtils.class.getSimpleName(), ex);
        }
        return strDecrypt;
    }

    public static String encryptRSA(String content, String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        return encryptRSA(content, decodePublicKey(pubKey, RSA_ALGORITHM));
    }

    public static String decryptRSA(String cipherContent, String priKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
        return decryptRSA(cipherContent, decodePrivateKey(priKey, RSA_ALGORITHM));
    }

    public static String encryptRSA(String content, Key pubKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        byte[] contentBytes = content.getBytes();
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherContent = cipher.doFinal(contentBytes);
        return Base64.getEncoder().encodeToString(cipherContent);
    }

    public static String decryptRSA(String cipherContent, Key priKey) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] cipherContentBytes = Base64.getDecoder().decode(cipherContent);
        byte[] decryptedContentBytes = cipher.doFinal(cipherContentBytes);
        return new String(decryptedContentBytes, StandardCharsets.UTF_8);
    }

    public static PublicKey decodePublicKey(String keyStr, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(spec);
    }

    public static PrivateKey decodePrivateKey(String keyStr, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(spec);
    }

    public static void createRSAKey() {
        createRSAKey(null);
    }

    public static void createRSAKey(String pathDir) {
        if (pathDir == null) pathDir = DEFAULT_DIR_PATH;
        String pathPubKey = pathDir.concat("publicKey.rsa");
        String pathPriKey = pathDir.concat("privateKey.rsa");
        try {
            SecureRandom sr = new SecureRandom();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, sr);
            KeyPair kp = kpg.genKeyPair();
            PublicKey publicKey = kp.getPublic();
            PrivateKey privateKey = kp.getPrivate();
            File publicKeyFile = createKeyFile(new File(pathPubKey));
            File privateKeyFile = createKeyFile(new File(pathPriKey));

            try (FileOutputStream fosPub = new FileOutputStream(publicKeyFile);
                 FileOutputStream fosPri = new FileOutputStream(privateKeyFile)) {
                fosPub.write(publicKey.getEncoded());
                fosPri.write(privateKey.getEncoded());
            }
            log.info("Generate key successfully");
        } catch (Exception e) {
            log.error("{} generate key error {}", EncryptRSAUtils.class.getSimpleName(), e);
        }
    }

    private static File createKeyFile(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    private Path makeDir(String filePath) {
        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            return path;
        } catch (Exception e) {
            log.error("{} create dir error {}", EncryptRSAUtils.class.getSimpleName(), e);
            throw new RuntimeException("Create dir error");
        }
    }

    public static void createRSAKeyAsString(String path) throws NoSuchAlgorithmException, IOException {
        if (path == null) path = "src/main/resources/rsa/";
        SecureRandom sr = new SecureRandom();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048, sr);
        KeyPair kp = kpg.genKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.concat("public_key_str")));
             BufferedWriter writer2 = new BufferedWriter(new FileWriter(path.concat("public_key_str")))){
            writer.write(publicKeyStr);
            writer2.write(privateKeyStr);
        }
    }
}
