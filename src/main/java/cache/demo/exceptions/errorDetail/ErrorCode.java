package cache.demo.exceptions.errorDetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCode {

  public static final int MERCHANT_NOT_FOUND = 001;
  public static final int CACHE_NOT_FOUND = 002;
  public static final int USER_NOT_FOUND = 003;
  public static final int CAN_NOT_LOG_IN = 004;



}
