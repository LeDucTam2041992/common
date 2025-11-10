package com.kpro.common.sercurity.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kpro.security")
public class PublicPathConfigProperties {
  private List<String> publicPaths = new ArrayList<>();

  public PublicPathConfigProperties() {}

  public PublicPathConfigProperties(List<String> publicPaths) {
    this.publicPaths = publicPaths;
  }

  public List<String> getPublicPaths() {
    return publicPaths;
  }

  public void setPublicPaths(List<String> publicPaths) {
    this.publicPaths = publicPaths;
  }
}
