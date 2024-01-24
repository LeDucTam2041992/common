package com.kpro.common.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;

public class ApiMessage {

    @Schema(example = "SUCCESS")
    private String code;
    @Schema(example = "SUCCESS")
    private String message;

    public ApiMessage() {
    }

    public ApiMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiMessage{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
