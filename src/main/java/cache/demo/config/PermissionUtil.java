package cache.demo.config;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;

public class PermissionUtil {

	private static final String PERMISSION_AUTHORITY_PREFIX = "PERMISSION_";

	private PermissionUtil() { }

	public static String permissionToAuthority(String permission) {
		if (!isAuthorityPermission(permission)) {
			permission = prependPermissionPrefix(permission);
		}
		return permission;
	}

	public static List<String> getPermissions(Authentication auth) {
		return auth.getAuthorities().stream()
				.filter(x -> isAuthorityPermission(x.getAuthority()))
				.map(p -> stripPermissionPrefixFromAuthority(p.getAuthority()))
				.collect(Collectors.toList());
	}

	private static boolean isAuthorityPermission(String authority) {
		return authority.startsWith(PERMISSION_AUTHORITY_PREFIX);
	}

	private static String stripPermissionPrefixFromAuthority(String authority) {
		return authority.substring(PERMISSION_AUTHORITY_PREFIX.length());
	}

	private static String prependPermissionPrefix(String permission) {
		return PERMISSION_AUTHORITY_PREFIX + permission;
	}
}
