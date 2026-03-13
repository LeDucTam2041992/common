package com.kpro.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AESUtils {
  private static final int KEY_SIZE = 128; // bits
  private static final int IV_SIZE = 12; // bytes (96 bits)
  private static final int TAG_SIZE = 128; // bits

  public static SecretKey generateKey() throws Exception {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(KEY_SIZE);
    return keyGen.generateKey();
  }

  public static byte[] generateIV() {
    byte[] iv = new byte[IV_SIZE];
    new SecureRandom().nextBytes(iv);
    return iv;
  }

  public static String encrypt(String plainText, SecretKey key, byte[] iv) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encrypted);
  }

  public static String decrypt(String cipherText, SecretKey key, byte[] iv) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE, iv);
    cipher.init(Cipher.DECRYPT_MODE, key, spec);
    byte[] decoded = Base64.getDecoder().decode(cipherText);
    byte[] decrypted = cipher.doFinal(decoded);
    return new String(decrypted, StandardCharsets.UTF_8);
  }

  public static SecretKey getKeyFromString(String keyString) {
    byte[] keyBytes = Base64.getDecoder().decode(keyString);
    return new SecretKeySpec(keyBytes, "AES");
  }
}
