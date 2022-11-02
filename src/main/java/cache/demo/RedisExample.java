package cache.demo;

import static cache.demo.constains.StringConstants.REDIS_CACHE_MANAGER_1_CACHE_1;
import static cache.demo.constains.StringConstants.REDIS_TEMPLATE_STATUS_KEY;

import cache.demo.repository.MerchantRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisExample implements CommandLineRunner {

  private final RedisTemplate<String, String> myRedisTemplate;
//  private final RedisTemplate myRedisTemplate2;

  private final RedisCacheManager redisCacheManager1;

  private final MerchantRepository userRepository;

  private final String EXPIRE_TIME = "Expire time of cache: [{}] = [{}].";
  private final String CACHES_OF_CACHEMANAGER = "Caches of [{}] = [{}].";

  @Override
  public void run(String... args) throws Exception {
    // Set giá trị của REDIS_TEMPLATE_STATUS_KEY là "success"
    myRedisTemplate.opsForValue().set(REDIS_TEMPLATE_STATUS_KEY,"success");
    log.info(REDIS_TEMPLATE_STATUS_KEY + ": [{}]", myRedisTemplate.opsForValue().get(REDIS_TEMPLATE_STATUS_KEY));

//    myRedisTemplate.expire(REDIS_TEMPLATE_STATUS_KEY, 10, TimeUnit.SECONDS);
    log.info(EXPIRE_TIME, REDIS_TEMPLATE_STATUS_KEY, myRedisTemplate.getExpire(REDIS_TEMPLATE_STATUS_KEY, TimeUnit.SECONDS));

    // Cache list của RedisCacheManager
    log.info(CACHES_OF_CACHEMANAGER, redisCacheManager1.getClass().getName(), redisCacheManager1.getCacheNames());
    redisCacheManager1.getCache(REDIS_CACHE_MANAGER_1_CACHE_1).put("cache11_key1", "cache11_value");
    System.out.println("ahihi manager 1 cache1, value = "
        +redisCacheManager1.getCache(REDIS_CACHE_MANAGER_1_CACHE_1).get("cache11_key1").get().toString());

//    MerchantEntity newUser = userRepository.save(MerchantEntity.builder()
//            .userId("xxx")
//        .build());
//    System.out.println("dcm = " + newUser);

  }
}
