package com.kpro.common.servicemanager;

import java.util.function.Supplier;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler
    implements MethodSecurityExpressionHandler {

  private static final Logger log =
      LoggerFactory.getLogger(CustomMethodSecurityExpressionHandler.class);
  private final FunctionalAccessControl functionalAccessControl;

  public CustomMethodSecurityExpressionHandler(FunctionalAccessControl functionalAccessControl) {
    log.info("[CustomMethodSecurityExpressionHandler] configuration.");
    this.functionalAccessControl = functionalAccessControl;
  }

  //  @Override
  //  protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
  //      Authentication authentication, MethodInvocation invocation) {
  //    CustomMethodSecurityExpressionRoot root =
  //        new CustomMethodSecurityExpressionRoot(authentication, this.functionalAccessControl);
  //    root.setThis(invocation.getThis());
  //    root.setPermissionEvaluator(this.getPermissionEvaluator());
  //    root.setTrustResolver(this.getTrustResolver());
  //    root.setRoleHierarchy(this.getRoleHierarchy());
  //    return root;
  //  }

  /**
   * Upgrade springboot 3.5.9. In 3.5.x, Spring Security uses the AuthorizationManager architecture.
   * If you don't explicitly wire the EvaluationContext to your CustomRoot, Spring creates a
   * DefaultMethodSecurityExpressionHandler under the hood. Since the default root doesn't have a
   * checkPermission method, you get the EL1004E error.
   */
  @Override
  public EvaluationContext createEvaluationContext(
      Supplier<Authentication> authentication, MethodInvocation invocation) {
    StandardEvaluationContext context =
        (StandardEvaluationContext) super.createEvaluationContext(authentication, invocation);

    CustomMethodSecurityExpressionRoot root =
        new CustomMethodSecurityExpressionRoot(authentication.get(), this.functionalAccessControl);

    root.setThis(invocation.getThis());
    root.setPermissionEvaluator(this.getPermissionEvaluator());
    root.setTrustResolver(this.getTrustResolver());
    root.setRoleHierarchy(this.getRoleHierarchy());

    context.setRootObject(root);
    return context;
  }
}
