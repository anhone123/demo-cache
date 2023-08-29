package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class LimitNotGreaterThanZeroException extends ApplicationException{

  public LimitNotGreaterThanZeroException() {
    super(ErrorCode.LIMIT_NOT_GREATER_THAN_ZERO, ErrorMessage.LIMIT_NOT_GREATER_THAN_ZERO);
  }
}
