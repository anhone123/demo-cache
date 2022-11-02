package cache.demo.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtils {

  //Remove white space: 32
  private static final int[] printableASCIIWithoutWhiteSpaceBound = new int[]{33, 126};
  private static final int[] numberBound = new int[]{48, 57};
  private static final int[] upperLetterBound = new int[]{65, 90};
  private static final int[] lowerLetterBound = new int[]{97, 122};

  private static final Random rand = new SecureRandom();

  public static int nextInt(int bound) {
    return rand.nextInt(bound);
  }

  public static long nextLong() {
    return rand.nextLong();
  }

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


  /**
   * Return a bound like: 9, 99, 999. Depends on length
   */
  public static int generateBound(int length) {
    // Because int type has value from -2147483648 to 2147483647
    // So it can not generate an expected value with length is larger than 9.
    if (length < 1) {
      throw new IllegalArgumentException();
    }
    if (length > 9) {
      throw new IllegalArgumentException();
    }

    final int maxNumber = 9;
    int bound = 0;
    for (int i = 1; bound == 0 || String.valueOf(bound).length() < length; i *= 10) {
      bound += maxNumber * i;
    }
    return bound;
  }

  public static String nextStrings(int[] bounds, int length) {
    validateLength(length);
    return rand.ints(bounds[0], bounds[1])
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  public static String nextAlphanumeric(int length) {
    validateLength(length);
    return rand
        .ints(printableASCIIWithoutWhiteSpaceBound[0], printableASCIIWithoutWhiteSpaceBound[1])
        .filter(i ->
            (i >= upperLetterBound[0] && i <= upperLetterBound[1])
            || (i >= lowerLetterBound[0] && i <= lowerLetterBound[1])
            || (i >= numberBound[0] && i <= numberBound[1])
        )
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  public static String nextHexString(int length) {
    validateLength(length);
    StringBuilder sb = new StringBuilder();
    while(sb.length() < length){
      sb.append(Integer.toHexString(rand.nextInt()));
    }
    return sb.toString().toUpperCase(Locale.ROOT);
  }

  public static String nextUpperLetter(int length) {
    return nextStrings(upperLetterBound, length);
  }

  public static String nextLowerLetter(int length) {
    return nextStrings(lowerLetterBound, length);
  }

  public static String nextNumberString(int length) {
    return nextStrings(numberBound, length);
  }

  private static void validateLength(int length) {
    if (length < 1) {
      throw new IllegalArgumentException();
    }
  }


}
