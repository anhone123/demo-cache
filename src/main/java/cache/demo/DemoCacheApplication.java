package cache.demo;

import cache.demo.config.ApplicationPropertiesConfig;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ConfigurationPropertiesScan({"cache.demo.config"})
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@Slf4j
@RequiredArgsConstructor
public class DemoCacheApplication {

  private final ApplicationPropertiesConfig applicationProperties;

  public static void main(String[] args) {
    SpringApplication.run(DemoCacheApplication.class, args);
  }

  @Bean // Makes ZonedDateTime compatible with auditing fields
  public DateTimeProvider auditingDateTimeProvider() {
    return () -> Optional.of(ZonedDateTime.now());
  }

  @Bean
  public WebMvcConfigurer webConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedMethods("*")
            .exposedHeaders("*")
            .allowedHeaders("*")
        ;
      }
    };
  }

  @Bean
  @Lazy
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
