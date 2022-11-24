package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class UserNotFoundException extends ApplicationException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND, ErrorMessage.USER_NOT_FOUND);
  }

}
