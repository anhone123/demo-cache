package cache.demo.exceptions.errorDetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {

  public static final int MERCHANT_NOT_FOUND = 001;
  public static final int CACHE_NOT_FOUND = 002;

}
