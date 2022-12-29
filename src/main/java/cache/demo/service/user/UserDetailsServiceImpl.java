package cache.demo.service.user;

import cache.demo.entities.RoleEntity;
import cache.demo.entities.UserEntity;
import cache.demo.exceptions.errorDetail.ErrorMessage;
import cache.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  private static final String ROLE_PREFIX = "ROLE_";
  private static final String PERMISSION_PREFIX = "PERMISSION_";


  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

    Optional<UserEntity> existedUser = userRepository.findUserEntityByUserId(s);

    if (existedUser.isEmpty()) {
        log.error(ErrorMessage.USER_NOT_FOUND + " with userId: {}", s);
        throw new UsernameNotFoundException("User " + s + " was not found in the database");
    }
    UserEntity userEntity = existedUser.get();

    Set<RoleEntity> roles = userEntity.getRoles();

    List<GrantedAuthority> grantedAuthorityList = roles.stream()
        .map(role -> new SimpleGrantedAuthority(PERMISSION_PREFIX + ROLE_PREFIX + role.getCodeName()))
        .collect(Collectors.toList());

    UserDetails userDetails = new User(userEntity.getUserId(), userEntity.getUserPassword(), grantedAuthorityList);
    log.info("UserDetail found with userId/userName: {}", userDetails.getUsername());
    return userDetails;

  }
}
