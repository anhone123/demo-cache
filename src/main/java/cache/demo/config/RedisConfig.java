package cache.demo.config;

import static cache.demo.constains.StringConstants.REDIS_CACHE_MANAGER_1_CACHE_1;
import static cache.demo.constains.StringConstants.REDIS_CACHE_MANAGER_1_CACHE_2;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
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
  public RedisCacheManager redisCacheManager1(LettuceConnectionFactory lettuceConnectionFactory) {

    //<CacheNames, CacheConfigs>
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

    RedisCacheConfiguration cacheConfiguration1 = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(6))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

    RedisCacheConfiguration cacheConfiguration2 = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofSeconds(60))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

    cacheConfigurations.put(REDIS_CACHE_MANAGER_1_CACHE_1, cacheConfiguration1);
    cacheConfigurations.put(REDIS_CACHE_MANAGER_1_CACHE_2, cacheConfiguration2);

    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(lettuceConnectionFactory)
        .withInitialCacheConfigurations(cacheConfigurations)
        .build();

  }
}
