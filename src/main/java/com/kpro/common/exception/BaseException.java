package com.kpro.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"errorObject", "tagObject"})
public class BaseException extends RuntimeException {
    private ErrorObject errorObject;
    private Object tagObject;

    public BaseException() {
        super();
        this.setErrorObject(new ErrorObject());
    }

    public BaseException(String errorCode) {
        this.setErrorObject(ErrorHelper.buildResponse(errorCode));
    }

    public BaseException(String errorCode, Object tag) {
        this(errorCode);
        this.setTagObject(tag);
    }

    public BaseException(ErrorObject errorObject, Object tag) {
        this.errorObject = errorObject;
        this.tagObject = tag;
    }

    public BaseException(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public BaseException(String message, ErrorObject errorObject) {
        super(message);
        this.errorObject = errorObject;
    }

    public BaseException(String message, Throwable cause, ErrorObject errorObject) {
        super(message, cause);
        this.errorObject = errorObject;
    }

    public BaseException(Throwable cause, ErrorObject errorObject) {
        super(cause);
        this.errorObject = errorObject;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorObject errorObject) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorObject = errorObject;
    }

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public Object getTagObject() {
        return tagObject;
    }

    public void setTagObject(Object tagObject) {
        this.tagObject = tagObject;
    }
}
