package com.kpro.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"errorCode", "errorDesc", "errorMessage"})
public class ErrorObject {
    private String errorCode;
    private String errorDesc;
    private ErrorMessage errorMessage;

    public ErrorObject(String errorCode, String errorDesc, ErrorMessage errorMessage) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.errorMessage = errorMessage;
    }

    public ErrorObject() {
    }

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

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorObject{" +
                "errorCode='" + errorCode + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
