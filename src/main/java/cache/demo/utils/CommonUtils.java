package cache.demo.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonUtils {

  public static Pageable getPagination(Integer page, Integer size, Sort sort) {
    return PageRequest.of(page - 1, size, sort);
  }

  public static ZonedDateTime convertUTCDate(ZonedDateTime zonedDateTime) {
    ZonedDateTime dateFm = null;
    if (zonedDateTime != null) {
      dateFm = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
    }
    return dateFm;
  }

  public static String buildPatternKeyword(String keyword, boolean isToLowercase) {
    String escapeBackslash = "%" + keyword.replace("\\", "\\\\") + "%";
    return isToLowercase ? escapeBackslash.toLowerCase() : escapeBackslash;
  }

  public static String trimToNullThenBuildPatternKeyword(String keyword, boolean isToLowercase) {
    if (keyword == null) {
      return null;
    }

    keyword = keyword.trim();
    if (Boolean.TRUE.equals(isToLowercase)) {
      keyword = keyword.toLowerCase();
    }

    return !keyword.isEmpty() ? "%" + keyword.replace("\\", "\\\\") + "%" : null;
  }

  public static Long convertUSDToCent(Double usd) {
    return (long) (usd * 100);
  }

  public static Double convertCentToUSD(Long cent) {
    return Double.valueOf(cent) / 100;
  }

//  public static ZonedDateTime convertStringToZoneDateTime(String zonedDateTime) throws InvalidDatetimeFormatException {
//    try {
//      return ZonedDateTime.parse(zonedDateTime);
//    } catch (Exception e) {
//      throw new InvalidDatetimeFormatException(e);
//    }
//  }

  public static String randomStringUUID() {
    return randomStringUUID(false);
  }

  public static String randomStringUUID(boolean removeDash) {
    var uuid = UUID.randomUUID().toString();

    if (removeDash) {
      return uuid.replace("-", "");
    }

    return uuid;
  }

  public static boolean isContain(String originValue, String checkValue) {
    return null != originValue && originValue.toLowerCase().contains(checkValue.toLowerCase());
  }
}
