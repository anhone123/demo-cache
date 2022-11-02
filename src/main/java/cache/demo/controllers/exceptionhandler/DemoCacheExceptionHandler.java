package cache.demo.controllers.exceptionhandler;

import cache.demo.exceptions.ApplicationException;
import cache.demo.exceptions.errorDetail.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
@ResponseBody
public class DemoCacheExceptionHandler {

  private static final String FOUND_AN_ERROR = "Found an error: {}";
  private static final String FOUND_AN_ERROR_INCLUDE_CODE = "Found an error: {} {}";

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private ErrorDetail handleApplicationException(ApplicationException ex) {
    log.error(FOUND_AN_ERROR, ex.getMessage(), ex);
    return ex.getErrorDetail();
  }

}
