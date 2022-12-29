package cache.demo.config;

import static cache.demo.config.PermissionUtil.permissionToAuthority;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * https://www.baeldung.com/spring-security-create-new-custom-security-expression
 */

/**
 * Re-implementation of Spring's MethodSecurityExpressionRoot. Would have liked
 * to extend instead, but alas they declared it as a package class, not public.
 * Their class hasn't changed since 3.0, so it's probably pretty stable.
 * <p>
 * This implementation provides new methods "hasPermission" and
 * "hasAnyPermission" in order to make life with our permissions system a little
 * clearer. This allows us to write
 * <code>@PreAuthorize("hasAnyPermission('ThisPerm', 'ThatPerm')")</code>
 * instead of
 * <code>@PreAuthorize("hasAnyAuthority('PERM_ThisPerm', 'PERM_ThatPerm')")</code>
 *
 * @see org.springframework.security.access.expression.method.MethodSecurityExpressionRoot
 */
@Slf4j
public class CustomMethodSecurityExpressionRoot
    extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

  private Object filterObject;
  private Object returnObject;
  private Object target;

  public CustomMethodSecurityExpressionRoot(Authentication authentication) {
    super(authentication);
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

  /**
   * Sets the "this" property for use in expressions. Typically this will be the "this"
   * property of the {@code JoinPoint} representing the method invocation which is being
   * protected.
   * @param target the target object on which the method in is being invoked.
   */
  void setThis(Object target) {
    this.target = target;
  }

  /**
   * Create custom permission security check
   */
  public boolean hasPermissionNiNe(String permission) {
    log.info("Method security check for permission: {}", permission);
    if (null == permission || permission.isBlank()) {
      return false;
    }
    return hasAuthority(permissionToAuthority(permission));
  }

  public boolean coPermissionNaoCungDuocNe(String... permissions) {
    if (null == permissions) {
      return false;
    }
    return hasAnyAuthority(
        Arrays.stream(permissions)
            .map(PermissionUtil::permissionToAuthority)
            .toArray(String[]::new)
    );
  }

  /**
   * hasPermission(), hasAnyPermission() can be overriden here, for example
   */
//  public boolean hasPermission(String permission) {
//    if (null == permission || permission.isBlank()) {
//      return false;
//    }
//    return hasAuthority(permissionToAuthority(permission));
//  }
//
//  public boolean hasAnyPermission(String... permissions) {
//    if (null == permissions) {
//      return false;
//    }
//    return hasAnyAuthority(
//        Arrays.stream(permissions)
//            .map(PermissionUtil::permissionToAuthority)
//            .toArray(String[]::new)
//    );
//  }

  /**
   * create method expression to check if the principal is a member of a Organization or a Group by id
   * Then apply in method:
   * @PreAuthorize("isMember(#id)")
   * @GetMapping("/organizations/{id}")
   * @ResponseBody
   * public Organization findOrgById(@PathVariable long id) {
   *     return organizationRepository.findOne(id);
   * }
   */
//  public boolean isMember(Long id) {
//    User user = ((MyUserPrincipal) this.getPrincipal()).getUser();
//    return user.getOrganization().getId().longValue() == OrganizationId.longValue();
//  }
}
