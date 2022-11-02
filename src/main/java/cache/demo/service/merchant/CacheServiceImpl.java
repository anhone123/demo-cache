package cache.demo.service.merchant;

import static cache.demo.constains.CacheNames.CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS;

import cache.demo.exceptions.ApplicationException;
import cache.demo.exceptions.CacheNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

  private final RedisCacheManager redisCacheManager;

  public Object getCacheValueBy(String cacheName, String key) throws ApplicationException {

    Cache cache = redisCacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException();
    }

    ValueWrapper valueWrapper;

    if (CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS.equals(cacheName)) {
      log.warn(CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS + " has no cache key specificed, replace key word with SimpleKey.EMPTY");
      valueWrapper = cache.get(SimpleKey.EMPTY);
    } else {
      valueWrapper = cache.get(key);
    }

    if (valueWrapper == null) {
      throw new ApplicationException(003, String.format("valueWrapper of key: %s not found", key));
    }

    return valueWrapper.get();
  }

  public void setCacheValue(String cacheName, String key, String stringValue)
      throws CacheNotFoundException {
    Cache cache = redisCacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException();
    }

    cache.put(key, stringValue);
    log.warn("Value of cache: {} has been set with key: {}!", cacheName, key);

  }

  public void clearAllCacheValueOf(String cacheName) throws CacheNotFoundException {
    Cache cache = redisCacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException();
    }
    cache.clear();
    log.warn("All values of cache: {} cleared!", cacheName);
  }

  public void clearCacheValueBy(String cacheName, String key) throws CacheNotFoundException {
    Cache cache = redisCacheManager.getCache(cacheName);
    if (cache == null) {
      throw new CacheNotFoundException();
    }
    cache.evict(key);
    log.warn("Value of cache: {} cleared with key: {}!", cacheName, key);
  }

}
