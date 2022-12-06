package cache.demo.config;

import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {
  /**
   * instead of using the more hardcoded version:
   * @PostAuthorize("hasAuthority('FOO_READ_PRIVILEGE')")
   * We can use use:
   * @PostAuthorize("hasPermission(returnObject, 'read')")
   * or
   * @PreAuthorize("hasPermission(#id, 'Foo', 'read')")
   */

//@PreAuthorize("hasPermission('ROLE', 'EDITOR')") dat o method, nhu nay chua linh hoat lam
// can config kieu @PreAuthorize("hasPermission('ROLE_EDITOR')") ok hon
  @Override
  public boolean hasPermission(
      Authentication authentication, Object targetDomainObject, Object permission) {

    if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)){
      return false;
    }
    String prefix = targetDomainObject.toString().toUpperCase();
    log.info("Method security check with prefix: {} and permission: {}", prefix, permission);

    return hasPrivilege(authentication, prefix, permission.toString().toUpperCase());
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Serializable targetId, String targetType, Object permission) {

    if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
      return false;
    }

    log.info("Method security check with targetType: {} and permission: {}", targetType, permission);

    return hasPrivilege(authentication, targetType.toUpperCase(), permission.toString().toUpperCase());
  }

  private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
    for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
      if (grantedAuth.getAuthority().contains(targetType) &&
          grantedAuth.getAuthority().contains(permission)) {
        return true;
      }
    }
    return false;
  }
}
