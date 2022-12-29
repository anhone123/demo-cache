package cache.demo.exceptions.errorDetail;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {

  public static final String MERCHANT_NOT_FOUND = "Merchant not found";
  public static final String MERCHANT_NOT_FOUND_INTERNATIONAL_CODE = "international.MERCHANT_NOT_FOUND";
  public static final String CACHE_NOT_FOUND = "Cache not found";
  public static final String USER_NOT_FOUND = "User not found";
  public static final String CAN_NOT_LOG_IN = "Can not log in!";

  public static final String BINDING_RESULT_ERROR = "BindingResult has error(s) !";


}
