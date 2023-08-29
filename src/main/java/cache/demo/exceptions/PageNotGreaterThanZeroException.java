package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class PageNotGreaterThanZeroException extends ApplicationException{

  public PageNotGreaterThanZeroException() {
    super(ErrorCode.PAGE_NOT_GREATER_THAN_ZERO, ErrorMessage.PAGE_NOT_GREATER_THAN_ZERO);
  }
}
