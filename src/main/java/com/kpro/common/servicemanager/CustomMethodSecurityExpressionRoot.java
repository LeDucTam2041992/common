package com.kpro.common.servicemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private static final Logger log = LoggerFactory.getLogger(CustomMethodSecurityExpressionRoot.class);
    private FunctionalAccessControl functionalAccessControl;
    private Object filterObject;
    private Object returnObject;
    private Object target;
    private static final String ADMIN = "tamld";

    public CustomMethodSecurityExpressionRoot(Authentication authentication, FunctionalAccessControl functionalAccessControl) {
        super(authentication);
        this.functionalAccessControl = functionalAccessControl;
    }

    public boolean checkPermission(String serviceName, String permission) {
        String username;
        var authentication = this.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            if (authentication instanceof AnonymousAuthenticationToken) {
                username = ADMIN;
            } else {
                if (!(authentication.getPrincipal() instanceof String)) {
                    return false;
                }
                username = (String) this.getAuthentication().getPrincipal();
            }
        }
        return this.functionalAccessControl.doCheckPermission(username, serviceName, permission);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    void setThis(Object target) {
        this.target = target;
    }
}
