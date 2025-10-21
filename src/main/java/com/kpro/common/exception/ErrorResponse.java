package com.kpro.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"errorCode", "errorDesc", "errorMessage"})
public class ErrorResponse {
  private String errorCode;
  private String errorDesc;
  private Map<String, String> errorMessage;
  private Object tagObject;

  public ErrorResponse(
      String errorCode, String errorDesc, Map<String, String> errorMessage, Object tagObject) {
    this.errorCode = errorCode;
    this.errorDesc = errorDesc;
    this.errorMessage = errorMessage;
    this.tagObject = tagObject;
  }

  public ErrorResponse() {}

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorDesc() {
    return errorDesc;
  }

  public void setErrorDesc(String errorDesc) {
    this.errorDesc = errorDesc;
  }

  public Map<String, String> getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(Map<String, String> errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Object getTagObject() {
    return tagObject;
  }

  public void setTagObject(Object tagObject) {
    this.tagObject = tagObject;
  }

  @Override
  public String toString() {
    return "ErrorObject{"
        + "errorCode='"
        + errorCode
        + '\''
        + ", errorDesc='"
        + errorDesc
        + '\''
        + ", errorMessage="
        + errorMessage
        + ", tagObject="
        + tagObject
        + '}';
  }
}
