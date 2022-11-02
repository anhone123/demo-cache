package cache.demo.controllers.endpoint;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DemoCacheEndpoint {

  public static final String MERCHANT = "merchant";
  public static final String MERCHANT_TRANSACTIONS = "/transactions";
  public static final String MERCHANT_ALL_TRANSACTIONS = "/all-transactions";
  public static final String MERCHANT_ALL_TRANSACTIONS_DUP_CACHE_KEY = "/all-transactions-dup-cache-key";

  public static final String CACHE_MANAGEMENT = "cache-management";
  public static final String CACHE_GET = "/get";
  public static final String CACHE_UPDATE = "/update";
  public static final String CACHE_CLEAR_BY_KEY = "/clear";
  public static final String CACHE_CLEAR_ALL = "/clear-all";



  public static final List<String> PROTECTED_URL = List.of(
  );
}
