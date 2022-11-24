package cache.demo.service.user;

import cache.demo.dto.user.UserLogInRequest;
import cache.demo.dto.user.UserLogInResponse;
import cache.demo.entities.UserEntity;
import cache.demo.exceptions.CanNotLogInException;
import cache.demo.exceptions.UserNotFoundException;
import cache.demo.repository.UserRepository;
import cache.demo.utils.RandomUtils;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserLogInResponse logIn(UserLogInRequest request)
      throws UserNotFoundException, CanNotLogInException {

    Optional<UserEntity> existedUser = userRepository.findUserEntityByUserId(request.getUserId());

    if (existedUser.isEmpty()) {
      throw new UserNotFoundException();
    }

    if (!passwordEncoder.matches(request.getUserPassword(), existedUser.get().getUserPassword())) {
      throw new CanNotLogInException();
    }

    UserEntity loggedInUser = existedUser.get();
    loggedInUser.setSessionToken(RandomUtils.randomStringUUID(true));
    loggedInUser.setSessionExpireDate(ZonedDateTime.now().plusMinutes(15));
    userRepository.save(loggedInUser);

    return UserLogInResponse.builder()
        .success(true)
        .sessionToken(loggedInUser.getSessionToken())
        .sessionExpireDate(loggedInUser.getSessionExpireDate())
        .build();
  }

  @Override
  public UserEntity getUserByUserId(String userId) throws UserNotFoundException {

    Optional<UserEntity> existedUser = userRepository.findUserEntityByUserId(userId);

    if (existedUser.isEmpty()) {
      throw new UserNotFoundException();
    }

    return existedUser.get();
  }

}
