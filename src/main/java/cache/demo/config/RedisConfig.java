package cache.demo.config;

import static cache.demo.constains.CacheNames.CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS;
import static cache.demo.constains.CacheNames.CACHE_TRANSACTIONS_OF_A_MERCHANT;
import static cache.demo.constains.CacheNames.MINOR_REDIS_CACHEMANAGER_CACHE_1;
import static cache.demo.constains.CacheNames.MINOR_REDIS_CACHEMANAGER_CACHE_2;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableCaching
@EnableScheduling
@RequiredArgsConstructor
public class RedisConfig {

  private final ApplicationPropertiesConfig applicationProperties;

//  thư viện mã nguồn mở, giúp kết nối tới Redis một cách thread-safe bằng nhiều
//  hình thức như synchronous, asynchronous and reactive usage.
//  Có thể sử dụng nhiều connector khác nhau.
//  https://docs.spring.io/spring-data/data-redis/docs/1.1.1.RELEASE/reference/html/redis.html
  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(
        applicationProperties.getRedisConfig().getHost(),
        applicationProperties.getRedisConfig().getPort()));
  }

  @Bean
  public RedisTemplate<Object, Object> myRedisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
    // tạo ra một RedisTemplate
    // Với Key là Object
    // Value là Object
    // RedisTemplate giúp chúng ta thao tác với Redis
    RedisTemplate<Object, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory);
    return template;
  }

//  @Bean
//  public RedisTemplate<Object, Object> myRedisTemplate2(RedisConnectionFactory lettuceConnectionFactory) {
//    RedisTemplate<Object, Object> template = new RedisTemplate<>();
//    template.setConnectionFactory(lettuceConnectionFactory);
//    return template;
//  }

  @Bean
  public RedisCacheManager minorRedisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {

    //<CacheNames, CacheConfigs>
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    RedisCacheConfiguration cacheConfiguration1 = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(120))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

    RedisCacheConfiguration cacheConfiguration2 = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(120))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

    cacheConfigurations.put(MINOR_REDIS_CACHEMANAGER_CACHE_1, cacheConfiguration1);
    cacheConfigurations.put(MINOR_REDIS_CACHEMANAGER_CACHE_2, cacheConfiguration2);

    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(lettuceConnectionFactory)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();
  }

  @Bean
  @Primary
  public RedisCacheManager mainRedisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {

    //<CacheNames, CacheConfigs>
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    // Không hỗ trợ config max size cho cache, tối đa 512MB string - text hoặc binary data
    // và giới hạn cắn tối đa 4GB ram.
    RedisCacheConfiguration allTransactionsCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(120));

    RedisCacheConfiguration transactionsOfAMerchantCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(120));

    cacheConfigurations.put(CACHE_ALL_TRANSACTIONS_OF_ALL_MERCHANTS, allTransactionsCacheConfig);
    cacheConfigurations.put(CACHE_TRANSACTIONS_OF_A_MERCHANT, transactionsOfAMerchantCacheConfig);

    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(lettuceConnectionFactory)
        .withInitialCacheConfigurations(cacheConfigurations)
        .disableCreateOnMissingCache() //Nếu get cache theo tên k được thì tạo mới cache => Disabled
        .build();

  }
}
