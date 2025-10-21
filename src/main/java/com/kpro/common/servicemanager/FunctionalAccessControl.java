package com.kpro.common.servicemanager;

public interface FunctionalAccessControl {
  boolean doCheckPermission(String username, String resource, String function, String action);
}
