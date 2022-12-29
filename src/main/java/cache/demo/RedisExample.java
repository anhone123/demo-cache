package cache.demo;

import static cache.demo.constains.CacheNames.MINOR_REDIS_CACHEMANAGER_CACHE_1;
import static cache.demo.constains.StringConstants.REDIS_TEMPLATE_STATUS_KEY;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisExample implements CommandLineRunner {

  private final RedisTemplate<String, String> myRedisTemplate;

  @Qualifier("minorRedisCacheManager")
  @Autowired // NOT recomment
  private final RedisCacheManager redisCacheManager;

  private final String EXPIRE_TIME = "Expire time of cache: [{}] = [{}].";
  public static final String CACHES_OF_CACHEMANAGER_TEMPLATE = "Caches of [{}] = {}.";

  @Override
  public void run(String... args) throws InterruptedException {
    // Set giá trị của REDIS_TEMPLATE_STATUS_KEY là "success"
    myRedisTemplate.opsForValue().set(REDIS_TEMPLATE_STATUS_KEY,"success");
    log.info(REDIS_TEMPLATE_STATUS_KEY + ": [{}]", myRedisTemplate.opsForValue().get(REDIS_TEMPLATE_STATUS_KEY));

    myRedisTemplate.expire(REDIS_TEMPLATE_STATUS_KEY, 10, TimeUnit.SECONDS);
    log.info(EXPIRE_TIME, REDIS_TEMPLATE_STATUS_KEY, myRedisTemplate.getExpire(REDIS_TEMPLATE_STATUS_KEY, TimeUnit.SECONDS));

    Thread.sleep(10000);
    log.info(REDIS_TEMPLATE_STATUS_KEY + ": [{}]", myRedisTemplate.opsForValue().get(REDIS_TEMPLATE_STATUS_KEY));

//
//    log.info(CACHES_OF_CACHEMANAGER_TEMPLATE, redisCacheManager.getClass().getName(), redisCacheManager.getCacheNames());
//    redisCacheManager.getCache(MINOR_REDIS_CACHEMANAGER_CACHE_1).put("Key1", "value1");
//    log.info(MINOR_REDIS_CACHEMANAGER_CACHE_1 + ", value = " + redisCacheManager.getCache(MINOR_REDIS_CACHEMANAGER_CACHE_1).get("Key1").get().toString());


  }
}
