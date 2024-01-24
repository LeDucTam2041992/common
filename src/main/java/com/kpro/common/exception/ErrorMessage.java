package com.kpro.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"vn", "en"})
public class ErrorMessage {
    private String en;
    private String vn;

    public ErrorMessage() {
    }

    public ErrorMessage(String en, String vn) {
        this.en = en;
        this.vn = vn;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "en='" + en + '\'' +
                ", vn='" + vn + '\'' +
                '}';
    }
}
