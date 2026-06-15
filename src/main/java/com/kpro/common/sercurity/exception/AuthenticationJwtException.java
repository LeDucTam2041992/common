package com.kpro.common.sercurity.exception;

import com.kpro.common.exception.BaseException;
import com.kpro.common.exception.KproErrorCode;

public class AuthenticationJwtException extends BaseException {

  public AuthenticationJwtException() {}

  public AuthenticationJwtException(KproErrorCode errorCode) {
    super(errorCode);
  }

  public AuthenticationJwtException(KproErrorCode errorCode, Object... args) {
    super(errorCode, args);
  }
}
