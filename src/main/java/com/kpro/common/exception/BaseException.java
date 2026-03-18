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
  private Object tag;
  private Object[] args;

  public BaseException() {}

  public BaseException(KproErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public BaseException(KproErrorCode errorCode, Object tag) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.tag = tag;
  }

  public BaseException(KproErrorCode errorCode, Object tag, Object[] args) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.tag = tag;
    this.args = args;
  }

  public KproErrorCode getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(KproErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

  public Object getTag() {
    return tag;
  }

  public void setTag(Object tag) {
    this.tag = tag;
  }

  public HttpStatus getStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
