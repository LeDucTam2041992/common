package com.kpro.common.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ECCUtils {
  private static final Logger log = LoggerFactory.getLogger(ECCUtils.class);

  private static final String PROVIDER = "BC";
  private static final int AES_KEY_SIZE_BYTES = 16; // 128 bit AES Key
//  private static final int AES_IV_SIZE_BYTES = 16; // 128 bit IV cho CBC mode AES/CBC/NoPadding
  private static final int AES_IV_SIZE_BYTES = 12; // 96 bit IV cho GCM mode AES/GCM/NoPadding (recommend use)

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static void main(String[] args) throws Exception {
    String priKeyMe =
        "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgkcupQuTPZMG1GPP+x1ETpw4LUDDvz0dpZWphT4BUrwCgCgYIKoZIzj0DAQehRANCAASDHB8x/3IfMwT6Mi9B/SNpBwx3N0aP19CL26viMJXt5Jzy4TcIIHKwJ0/XFRyukyRYPs9KPRbEMVWx+VsUnfZD";
    String pubKeyKPro =
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEBPHT/6KflfVfCokCf2hnDJzyf5y8AaWDSgICtGAhVGjnQLWZIfKNmu48fkm5Pfgu+uT0rNR+TJ2SbDoPmTJXbg==";

    String priKeyKPro =
        "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgXG/FsUcq9cnOr4wDE6viTn+nMeQi5qJUJA+HO0Oo2zqgCgYIKoZIzj0DAQehRANCAAQE8dP/op+V9V8KiQJ/aGcMnPJ/nLwBpYNKAgK0YCFUaOdAtZkh8o2a7jx+Sbk9+C765PSs1H5MnZJsOg+ZMldu";
    String pubKeyMe =
        "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEgxwfMf9yHzME+jIvQf0jaQcMdzdGj9fQi9ur4jCV7eSc8uE3CCBysCdP1xUcrpMkWD7PSj0WxDFVsflbFJ32Qw==";

    String content_me = "call me lazy";
    String encodeContentMe = encode(priKeyMe, pubKeyKPro, content_me);
    log.info("----- Encrypt from me -----");
    log.info(encodeContentMe);
    String decodeContentMe = decode(priKeyKPro, pubKeyMe, encodeContentMe);
    log.info("----- Decode from KPro -----");
    log.info(decodeContentMe);
  }

  public static String encode(String priKey, String pubKey, String content)
      throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
          NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
          InvalidKeyException, NoSuchProviderException {

    // 1. Sinh ngẫu nhiên IV an toàn tuyệt đối (Không cố định theo Shared Secret nữa)
    byte[] iv = new byte[AES_IV_SIZE_BYTES];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    // 2. Lấy Cipher AES đã được cấu hình với SessionKey sinh từ ECDH và IV ngẫu nhiên
    Cipher cipher = getCipher(priKey, pubKey, ivSpec, Cipher.ENCRYPT_MODE);

    byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
    byte[] cipherContent = cipher.doFinal(contentBytes);

    // 3. Nối [IV] + [Dữ liệu đã mã hóa] lại với nhau
    byte[] combined = new byte[iv.length + cipherContent.length];
    System.arraycopy(iv, 0, combined, 0, iv.length);
    System.arraycopy(cipherContent, 0, combined, iv.length, cipherContent.length);

    // 4. Trả về chuỗi Base64 hoàn chỉnh
    return Base64.getEncoder().encodeToString(combined);
  }

  public static String decode(String priKey, String pubKey, String base64CipherText)
      throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
          NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
          InvalidKeyException, NoSuchProviderException {

    byte[] combined = Base64.getDecoder().decode(base64CipherText);

    // 1. Tách IV ra khỏi phần đầu của mảng byte
    byte[] iv = Arrays.copyOfRange(combined, 0, AES_IV_SIZE_BYTES);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    // 2. Tách phần dữ liệu bị mã hóa thực sự ra
    byte[] cipherContent = Arrays.copyOfRange(combined, AES_IV_SIZE_BYTES, combined.length);

    // 3. Khởi tạo Cipher ở chế độ DECRYPT_MODE
    Cipher cipher = getCipher(priKey, pubKey, ivSpec, Cipher.DECRYPT_MODE);
    byte[][] decryptedBytes = {cipher.doFinal(cipherContent)};

    return new String(decryptedBytes[0], StandardCharsets.UTF_8);
  }

  private static Cipher getCipher(String priKey, String pubKey, IvParameterSpec ivSpec, int mode)
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
          NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException {

    // Thỏa thuận khóa thông qua ECDH
    KeyAgreement ka = KeyAgreement.getInstance("ECDH", PROVIDER);
    ka.init(decodePrivateKey(priKey, "ECDH", Security.getProvider(PROVIDER)));
    ka.doPhase(decodePublicKey(pubKey, "ECDH", Security.getProvider(PROVIDER)), true);
    byte[] sharedSecret = ka.generateSecret();

    // Băm chuỗi Shared Secret để tạo Key phái sinh sạch sẽ
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte[] digest = messageDigest.digest(sharedSecret);

    // Trích xuất chuỗi byte để làm Session Key cho AES (Lấy 16 bytes đầu cho AES-128)
    byte[] sessionKeyBytes = Arrays.copyOfRange(digest, 0, AES_KEY_SIZE_BYTES);
    SecretKey secretKey = new SecretKeySpec(sessionKeyBytes, "AES");

    // Sử dụng thuật toán AES tiêu chuẩn với chế độ GCM
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(mode, secretKey, ivSpec);

    return cipher;
  }

  private static Key decodePublicKey(String pubKey, String algorithm, Provider provider)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = Base64.getDecoder().decode(pubKey);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
    return keyFactory.generatePublic(spec);
  }

  private static Key decodePrivateKey(String priKey, String algorithm, Provider provider)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = Base64.getDecoder().decode(priKey);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
    return keyFactory.generatePrivate(spec);
  }

  public static void genECKey()
      throws InvalidAlgorithmParameterException, IOException, NoSuchAlgorithmException,
          NoSuchProviderException {
    genECKey(null);
  }

  public static void genECKey(String path)
      throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
          NoSuchProviderException {
    if (path == null) path = "src/main/resources/eec/";

    ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
    KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDH", PROVIDER);
    generator.initialize(spec, new SecureRandom());
    KeyPair keyPair = generator.generateKeyPair();
    ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
    ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
    String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
    String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.concat("public_key_str")));
        BufferedWriter writer2 =
            new BufferedWriter(new FileWriter(path.concat("private_key_str")))) {
      writer.write(publicKeyStr);
      writer2.write(privateKeyStr);
    }
  }
}
