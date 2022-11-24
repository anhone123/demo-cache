package cache.demo.service.cache;

import cache.demo.exceptions.ApplicationException;
import cache.demo.exceptions.CacheNotFoundException;

public interface CacheService {

  Object getCacheValueBy(String cacheName, String key) throws ApplicationException;

  void setCacheValue(String cacheName, String key, String stringValue) throws CacheNotFoundException;

  void clearAllCacheValueOf(String cacheName) throws CacheNotFoundException;

  void clearCacheValueBy(String cacheName, String key) throws CacheNotFoundException;

}
