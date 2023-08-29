package cache.demo.service.user;

import cache.demo.dto.user.GetUserResponsePayload.GetUserResponsePage;
import cache.demo.dto.user.UserLogInRequest;
import cache.demo.dto.user.UserLogInResponse;
import cache.demo.entities.UserEntity;
import cache.demo.exceptions.CanNotLogInException;
import cache.demo.exceptions.LimitNotGreaterThanZeroException;
import cache.demo.exceptions.PageNotGreaterThanZeroException;
import cache.demo.exceptions.PaginationRequestMissingException;
import cache.demo.exceptions.SortPropertyInvalidException;
import cache.demo.exceptions.UserNotFoundException;
import org.springframework.data.domain.Sort;

public interface UserService {

  UserLogInResponse logIn(UserLogInRequest request)
      throws UserNotFoundException, CanNotLogInException;

  UserEntity getUserByUserId(String userId) throws UserNotFoundException;

  GetUserResponsePage getAllUsersWithNplus1Issue(String keyword, Integer page,
      Integer size, Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException;

  GetUserResponsePage getAllUsersWith2Query(String keyword, Integer page, Integer size, Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException;

//  GetUserResponsePage getAllUsersWith1QueryAndReturnUserDtos(String keyword, Integer page, Integer size,
//      Sort sort)
//      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
//      PaginationRequestMissingException, SortPropertyInvalidException;

  GetUserResponsePage getAllUsersReturnUserProjectionInterfaceWith1JpaQuery(String keyword,
      Integer page, Integer size,
      Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException;

  GetUserResponsePage getAllUsersReturnUserProjectionInterfaceWith1NativeQuery(String keyword,
      Integer page, Integer size,
      Sort sort)
      throws LimitNotGreaterThanZeroException, PageNotGreaterThanZeroException,
      PaginationRequestMissingException, SortPropertyInvalidException;
}
