package com.kpro.common.sercurity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(
        prefix = "kpro.security.public-path"
)
public class PublicPathConfigProperties {
    private List<String> paths = new ArrayList<>();

    public PublicPathConfigProperties() {
    }

    public PublicPathConfigProperties(List<String> paths) {
        this.paths = paths;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
