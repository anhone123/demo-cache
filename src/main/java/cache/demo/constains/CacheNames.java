package cache.demo.constains;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheNames {

  public static final String CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS = "CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS";
  public static final String CACHE_TRANSACTIONS_OF_A_MERCHANT = "CACHE_TRANSACTIONS_OF_A_MERCHANT";

  public static final String MINOR_REDIS_CACHEMANAGER_CACHE_1 = "MINOR_REDIS_CACHEMANAGER_CACHE_1";
  public static final String MINOR_REDIS_CACHEMANAGER_CACHE_2 = "MINOR_REDIS_CACHEMANAGER_CACHE_2";

  public static final List<String> ALL_CACHE_NAMES =
      List.of(
          CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS,
          CACHE_TRANSACTIONS_OF_A_MERCHANT,
          MINOR_REDIS_CACHEMANAGER_CACHE_1,
          MINOR_REDIS_CACHEMANAGER_CACHE_2
      );

  public static final String ALL_CACHE_STRING_NAMES_SAMPLE = "Cache names: " + CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS
      + " | " + CACHE_TRANSACTIONS_OF_A_MERCHANT;


}
