package cache.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * https://www.baeldung.com/spring-security-create-new-custom-security-expression
 */

@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true,//Enable Spring’s pre/post annotations.
    securedEnabled = true,//Determine if the @Security annotation should be enabled.
    jsr250Enabled = true)//Allow us to use JSR250 based annotation (e.g. @RoleAllowed).
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
//    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler();

    //change default PermissionEvaluator = DenyAllPermissionEvaluator to CustomPermissionEvaluator.
    expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());

    return expressionHandler;
  }

}
