package com.kpro.common.servicemanager;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler implements MethodSecurityExpressionHandler {
    private final FunctionalAccessControl functionalAccessControl;

    public CustomMethodSecurityExpressionHandler(FunctionalAccessControl functionalAccessControl) {
        this.functionalAccessControl = functionalAccessControl;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication, this.functionalAccessControl);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(this.getPermissionEvaluator());
        root.setTrustResolver(this.getTrustResolver());
        root.setRoleHierarchy(this.getRoleHierarchy());
        return root;
    }
}
