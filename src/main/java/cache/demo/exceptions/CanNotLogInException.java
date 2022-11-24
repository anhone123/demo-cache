package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class CanNotLogInException extends ApplicationException {

  public CanNotLogInException() {
    super(ErrorCode.CAN_NOT_LOG_IN, ErrorMessage.CAN_NOT_LOG_IN);
  }

}
