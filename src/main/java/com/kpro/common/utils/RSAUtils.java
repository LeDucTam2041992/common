package com.kpro.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class RSAUtils {
  private static final Logger log = LoggerFactory.getLogger(RSAUtils.class);
  private static final String RSA_ALGORITHM = "RSA/None/OAEPWITHSHA-256ANDMGF1PADDING";
//  ECB -> for old version jdk, use None is modern
//  private static final String RSA_ALGORITHM = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

  public static String encryptRSA(String message) {
    String strEncrypt = null;
    try {
      byte[] b =
          RSAUtils.class
              .getClassLoader()
              .getResourceAsStream("publicKey.rsa")
              .readAllBytes();
      X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
      KeyFactory factory = KeyFactory.getInstance("RSA");
      PublicKey pubKey = factory.generatePublic(spec);
      Cipher c = Cipher.getInstance(RSA_ALGORITHM);
      c.init(Cipher.ENCRYPT_MODE, pubKey);
      byte[] encryptOut = c.doFinal(message.getBytes());
      strEncrypt = Base64.getEncoder().encodeToString(encryptOut);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return strEncrypt;
  }

  public static String decryptRSA(String encryptMessage) {
    String strDecrypt = null;
    try {
      byte[] b =
          RSAUtils.class
              .getClassLoader()
              .getResourceAsStream("privateKey.rsa")
              .readAllBytes();

      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
      KeyFactory factory = KeyFactory.getInstance("RSA");
      PrivateKey priKey = factory.generatePrivate(spec);

      Cipher c = Cipher.getInstance(RSA_ALGORITHM);
      c.init(Cipher.DECRYPT_MODE, priKey);

      byte[] decryptOut = c.doFinal(Base64.getDecoder().decode(encryptMessage));
      strDecrypt = new String(decryptOut);

    } catch (Exception ex) {
      log.error("[{}] decrypt error [{}]", RSAUtils.class.getSimpleName(), ex.getMessage());
    }
    return strDecrypt;
  }

  public static String encryptRSA(String content, String pubKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException,
          NoSuchPaddingException, BadPaddingException, InvalidKeyException {
    return encryptRSA(content, decodePublicKey(pubKey, RSA_ALGORITHM));
  }

  public static String decryptRSA(String cipherContent, String priKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException,
          NoSuchPaddingException, BadPaddingException, InvalidKeyException {
    return decryptRSA(cipherContent, decodePrivateKey(priKey, RSA_ALGORITHM));
  }

  public static String encryptRSA(String content, Key pubKey)
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
          NoSuchPaddingException, NoSuchAlgorithmException {
    byte[] contentBytes = content.getBytes();
    Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] cipherContent = cipher.doFinal(contentBytes);
    return Base64.getEncoder().encodeToString(cipherContent);
  }

  public static String decryptRSA(String cipherContent, Key priKey)
      throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException,
          NoSuchAlgorithmException, InvalidKeyException {
    Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, priKey);
    byte[] cipherContentBytes = Base64.getDecoder().decode(cipherContent);
    byte[] decryptedContentBytes = cipher.doFinal(cipherContentBytes);
    return new String(decryptedContentBytes, StandardCharsets.UTF_8);
  }

  public static PublicKey decodePublicKey(String keyStr, String algorithm)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = Base64.getDecoder().decode(keyStr);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    return keyFactory.generatePublic(spec);
  }

  public static PrivateKey decodePrivateKey(String keyStr, String algorithm)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = Base64.getDecoder().decode(keyStr);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
    return keyFactory.generatePrivate(spec);
  }
}
