package cache.demo.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application")
@ConstructorBinding
@Validated
public class ApplicationPropertiesConfig {

  @NotNull
  @Valid
  private final Token token;

  @NotNull
  @Valid
  private final RedisConfig redisConfig;

  @Getter
  @RequiredArgsConstructor
  public static class Token {

    @NotNull
    private final long expireHour;

  }

  @Getter
  @RequiredArgsConstructor
  public static class RedisConfig {

    @NotNull
    private final String host;
    @NotNull
    private final int port;

  }

}
