package cache.demo.service.user;

import cache.demo.dto.user.UserLogInRequest;
import cache.demo.dto.user.UserLogInResponse;
import cache.demo.entities.UserEntity;
import cache.demo.exceptions.CanNotLogInException;
import cache.demo.exceptions.UserNotFoundException;

public interface UserService {

  UserLogInResponse logIn(UserLogInRequest request)
      throws UserNotFoundException, CanNotLogInException;

  UserEntity getUserByUserId(String userId) throws UserNotFoundException;

}
