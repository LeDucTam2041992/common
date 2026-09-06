package com.kpro.common.communication;

import com.kpro.common.exception.BaseException;
import com.kpro.common.exception.KproErrorCode;

public class RestInternalTimeoutException extends BaseException {

  public RestInternalTimeoutException() {}

  public RestInternalTimeoutException(KproErrorCode errorCode) {
    super(errorCode);
  }

  public RestInternalTimeoutException(KproErrorCode errorCode, Object tag) {
    super(errorCode, tag);
  }

  public RestInternalTimeoutException(KproErrorCode errorCode, Object tag, Object... args) {
    super(errorCode, tag, args);
  }
}
