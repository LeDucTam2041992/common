package com.kpro.common.servicemanager;

import com.kpro.common.sercurity.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private static final Logger log =
        LoggerFactory.getLogger(CustomMethodSecurityExpressionRoot.class);
    private final FunctionalAccessControl functionalAccessControl;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, FunctionalAccessControl functionalAccessControl) {
        super(authentication);
        this.functionalAccessControl = functionalAccessControl;
    }

    public boolean checkPermission(String resource, String function, String action) {
        String username;
        var authentication = this.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            if (authentication instanceof AnonymousAuthenticationToken) {
                return false;
            } else {
                if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
                    username = userPrincipal.getUserId();
                } else {
                    return false;
                }
            }
        }
        return this.functionalAccessControl.doCheckPermission(username, resource, function, action);
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public void setThis(Object target) {
        this.target = target;
    }
}
