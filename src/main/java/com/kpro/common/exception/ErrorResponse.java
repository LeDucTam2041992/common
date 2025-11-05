package com.kpro.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"code", "desc", "errorMessage"})
public class ErrorResponse {
  private String code;
  private String desc;
  private Map<String, String> errorMessage;
  private Object tagObject;

  public ErrorResponse(
      String code, String desc, Map<String, String> errorMessage, Object tagObject) {
    this.code = code;
    this.desc = desc;
    this.errorMessage = errorMessage;
    this.tagObject = tagObject;
  }

  public ErrorResponse() {}

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
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
        + code
        + '\''
        + ", errorDesc='"
        + desc
        + '\''
        + ", errorMessage="
        + errorMessage
        + ", tagObject="
        + tagObject
        + '}';
  }
}
