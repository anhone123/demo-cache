package cache.demo.utils;

import cache.demo.exceptions.LimitNotGreaterThanZeroException;
import cache.demo.exceptions.PageNotGreaterThanZeroException;
import cache.demo.exceptions.PaginationRequestMissingException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidatorUtils {

  // This method doesn't check the page and size are null (suggest handle that case at controller)
  public void validatePaginationRequest(Integer page, Integer size)
      throws PageNotGreaterThanZeroException, LimitNotGreaterThanZeroException, PaginationRequestMissingException {

    final var bypassValidation = page == null && size == null;
    if (bypassValidation) {
      return;
    }

    final var mustValidateRequest = page != null && size != null;
    if (mustValidateRequest) {
      validatePageableRequest(page, size);
    } else {
      log.error("Pagination request is missing (page, size or limit)");
      throw new PaginationRequestMissingException();
    }
  }

  void validatePageableRequest(int page, int size)
      throws PageNotGreaterThanZeroException, LimitNotGreaterThanZeroException {
    if (page <= 0) {
      log.error("Page param not greater than 0!");
      throw new PageNotGreaterThanZeroException();
    }
    if (size <= 0) {
      log.error("Size param not greater than 0!");
      throw new LimitNotGreaterThanZeroException();
    }
  }

  public boolean isISOFormatDateTime(String text) {
    try {
      ZonedDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
