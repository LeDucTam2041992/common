package com.kpro.common.sercurity.exception;

import com.kpro.common.exception.BaseException;
import com.kpro.common.exception.KproErrorCode;

public class AuthenticationJwtException extends BaseException {
  private String principal;

  public AuthenticationJwtException() {}

  public AuthenticationJwtException(KproErrorCode errorCode) {
    super(errorCode);
  }

  public AuthenticationJwtException(KproErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }

  public AuthenticationJwtException(KproErrorCode errorCode, String principal) {
    super(errorCode);
    this.principal = principal;
  }

  public AuthenticationJwtException(KproErrorCode errorCode, String principal, Object... args) {
    super(errorCode, args);
    this.principal = principal;
  }
}
