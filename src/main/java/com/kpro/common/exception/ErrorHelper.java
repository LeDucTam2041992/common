package com.kpro.common.exception;

public class ErrorHelper {
    public static ErrorObject buildResponse(String errorCode) {
        return buildErrorObject(errorCode, null);
    }

    private static ErrorObject buildErrorObject(String errorCode, String errorDesc) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setErrorCode(errorCode);
        errorObject.setErrorDesc(errorDesc);
        errorObject.setErrorMessage(ErrorMessageLoader.getMessage(errorCode));
        return errorObject;
    }

    public static ErrorObject internalServerError() {
        return buildErrorObject("99999", null);
    }
}
