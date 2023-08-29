package cache.demo.exceptions.errorDetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {

  public static final int MERCHANT_NOT_FOUND = 001;
  public static final int CACHE_NOT_FOUND = 002;
  public static final int USER_NOT_FOUND = 003;
  public static final int CAN_NOT_LOG_IN = 004;

  public static final int LIMIT_NOT_GREATER_THAN_ZERO = 21;
  public static final int PAGE_NOT_GREATER_THAN_ZERO = 22;
  public static final int SORT_PROPERTY_INVALID = 52;
  public static final int PAGINATION_REQUEST_MISSING = 53;


  public static final int BINDING_RESULT_ERROR = 123;


}
