package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class CacheNotFoundException extends ApplicationException {

  public CacheNotFoundException() {
    super(ErrorCode.CACHE_NOT_FOUND, ErrorMessage.CACHE_NOT_FOUND);
  }

}
