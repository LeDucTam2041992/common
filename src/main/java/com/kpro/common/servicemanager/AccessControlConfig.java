package com.kpro.common.servicemanager;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kpro.security.access-control")
public class AccessControlConfig {
  private String urlCheckPermission =
      "http://service-manager.default.cluster.svc.local:8080/api/permissions/check?permission={permission}&username={username}";

  public AccessControlConfig() {}

  public AccessControlConfig(String urlCheckPermission) {
    this.urlCheckPermission = urlCheckPermission;
  }

  public String getUrlCheckPermission() {
    return urlCheckPermission;
  }

  public void setUrlCheckPermission(String urlCheckPermission) {
    this.urlCheckPermission = urlCheckPermission;
  }
}
