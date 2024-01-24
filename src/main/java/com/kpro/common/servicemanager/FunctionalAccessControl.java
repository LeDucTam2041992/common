package com.kpro.common.servicemanager;

public interface FunctionalAccessControl {
    boolean doCheckPermission(String username, String serviceName, String permission);
}
