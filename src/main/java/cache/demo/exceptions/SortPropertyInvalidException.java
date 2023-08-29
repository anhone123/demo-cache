package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class SortPropertyInvalidException extends ApplicationException {

  public SortPropertyInvalidException(Throwable rootCause) {
    super(ErrorCode.SORT_PROPERTY_INVALID, ErrorMessage.SORT_PROPERTY_INVALID, rootCause);
  }
}
