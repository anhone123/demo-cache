package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorCode;
import cache.demo.exceptions.errorDetail.ErrorMessage;

public class MerchantNotFoundException extends ApplicationException {

  public MerchantNotFoundException() {
    super(ErrorCode.MERCHANT_NOT_FOUND, ErrorMessage.MERCHANT_NOT_FOUND);
  }

}
