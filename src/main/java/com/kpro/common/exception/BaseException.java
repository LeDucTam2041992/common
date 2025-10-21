package com.kpro.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"errorObject"})
public class BaseException extends RuntimeException {

  private KproErrorCode errorCode;
  private Object[] args;
  private Object tag;

  public BaseException() {}

  public BaseException(KproErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public BaseException(KproErrorCode errorCode, Object... args) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.args = args;
  }

  public KproErrorCode getErrorCode() {
    return errorCode;
  }

  public Object[] getArgs() {
    return args;
  }

  public Object getTag() {
    return tag;
  }

  public HttpStatus getStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
