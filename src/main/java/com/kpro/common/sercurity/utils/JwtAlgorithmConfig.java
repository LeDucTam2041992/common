package com.kpro.common.sercurity.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kpro.security.jwt.config")
public class JwtAlgorithmConfig {
  private String internalPublicKey;
  private String internalPrivateKey;
  private String internalKeySign;
  private String algorithm = "HS256";
  private long expiredInSeconds;

  public JwtAlgorithmConfig() {}

  public JwtAlgorithmConfig(
      String internalPublicKey,
      String internalPrivateKey,
      String internalKeySign,
      String algorithm,
      long expiredInSeconds) {
    this.internalPublicKey = internalPublicKey;
    this.internalPrivateKey = internalPrivateKey;
    this.internalKeySign = internalKeySign;
    this.algorithm = algorithm;
    this.expiredInSeconds = expiredInSeconds;
  }

  public String getInternalPublicKey() {
    return internalPublicKey;
  }

  public void setInternalPublicKey(String internalPublicKey) {
    this.internalPublicKey = internalPublicKey;
  }

  public String getInternalPrivateKey() {
    return internalPrivateKey;
  }

  public void setInternalPrivateKey(String internalPrivateKey) {
    this.internalPrivateKey = internalPrivateKey;
  }

  public String getInternalKeySign() {
    return internalKeySign;
  }

  public void setInternalKeySign(String internalKeySign) {
    this.internalKeySign = internalKeySign;
  }

  public long getExpiredInSeconds() {
    return expiredInSeconds;
  }

  public void setExpiredInSeconds(long expiredInSeconds) {
    this.expiredInSeconds = expiredInSeconds;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }
}
