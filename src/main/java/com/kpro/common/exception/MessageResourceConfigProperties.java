package com.kpro.common.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "kpro.message.resource"
)
public class MessageResourceConfigProperties {
    private String enResourceUrl = "/message_en.properties";
    private String viResourceUrl = "/message_vn.properties";

    public MessageResourceConfigProperties() {
    }

    public MessageResourceConfigProperties(String enResourceUrl, String viResourceUrl) {
        this.enResourceUrl = enResourceUrl;
        this.viResourceUrl = viResourceUrl;
    }

    public String getEnResourceUrl() {
        return enResourceUrl;
    }

    public void setEnResourceUrl(String enResourceUrl) {
        this.enResourceUrl = enResourceUrl;
    }

    public String getViResourceUrl() {
        return viResourceUrl;
    }

    public void setViResourceUrl(String viResourceUrl) {
        this.viResourceUrl = viResourceUrl;
    }
}
