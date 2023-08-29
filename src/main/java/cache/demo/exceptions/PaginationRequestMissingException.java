package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class PaginationRequestMissingException extends ApplicationException {

  public PaginationRequestMissingException() {
    super(ErrorCode.PAGINATION_REQUEST_MISSING, ErrorMessage.PAGINATION_REQUEST_MISSING);
  }
}
