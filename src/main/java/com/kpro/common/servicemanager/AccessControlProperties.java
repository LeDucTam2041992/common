package com.kpro.common.servicemanager;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kpro.security.access-control")
public class AccessControlProperties {
  private String urlCheckPermission =
      "permission-manager-service:80/api/permissions/check?permission={permission}&username={username}";

  public AccessControlProperties() {}

  public AccessControlProperties(String urlCheckPermission) {
    this.urlCheckPermission = urlCheckPermission;
  }

  public String getUrlCheckPermission() {
    return urlCheckPermission;
  }

  public void setUrlCheckPermission(String urlCheckPermission) {
    this.urlCheckPermission = urlCheckPermission;
  }
}
