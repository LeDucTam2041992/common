package com.kpro.common.exception;

public enum KproCommonErrorCode implements KproErrorCode {
  INTERNAL_ERROR("COM_001", "Internal Server Error"),
  TOKEN_EXPIRED("COM_002", "Token expired"),
  TOKEN_INVALID("COM_003", "Token invalid"),
  S3_BUCKET_NOT_EXISTS("COM_004", "Storage Error");

  private final String code;
  private final String message;

  KproCommonErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
