package com.kpro.common.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SignUtils {

  /**
   * signature length (bytes) = keySizeInBits / 8
   * ex: 2048-bit RSA key, byte[] length = 256 bytes
   */
  public static byte[] signLargeFile(String filePath, PrivateKey privateKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initSign(privateKey);

    try (InputStream fis = new FileInputStream(filePath)) {
      byte[] buffer = new byte[8192];
      int nread;

      while ((nread = fis.read(buffer)) != -1) {
        signature.update(buffer, 0, nread);
      }
    }
    return signature.sign();
  }

  public static boolean verifyLargeFile(
      String filePath, byte[] signatureToVerify, PublicKey publicKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");

    signature.initVerify(publicKey);

    try (InputStream fis = new FileInputStream(filePath)) {
      byte[] buffer = new byte[8192];
      int nread;
      while ((nread = fis.read(buffer)) != -1) {
        signature.update(buffer, 0, nread);
      }
    }

    return signature.verify(signatureToVerify);
  }
}
