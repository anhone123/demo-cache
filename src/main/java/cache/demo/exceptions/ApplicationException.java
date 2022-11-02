package cache.demo.exceptions;

import cache.demo.exceptions.errorDetail.ErrorDetail;
import lombok.Getter;

@Getter
public class ApplicationException extends Exception {

  protected final transient ErrorDetail errorDetail;

  public ApplicationException(int errorCode, String errorMessage) {
    super(errorMessage);

    this.errorDetail = ErrorDetail.builder()
        .errorCode(errorCode)
        .errorMessage(errorMessage)
        .build();
  }

  public ApplicationException(int errorCode, String errorMessage, Throwable rootCause) {
    super(errorMessage, rootCause);

    this.errorDetail = ErrorDetail.builder()
        .errorCode(errorCode)
        .errorMessage(errorMessage)
        .build();
  }
}

