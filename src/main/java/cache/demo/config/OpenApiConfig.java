package cache.demo.config;

import cache.demo.controllers.endpoint.DemoCacheEndpoint;
import cache.demo.exceptions.errorDetail.ErrorDetail;
import com.fasterxml.classmate.TypeResolver;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class OpenApiConfig {

  private ApiKey apiKey() {
    return new ApiKey("Bearer", "Authorization", "header");
  }

//  private ApiKey basicApiKey() {
//    return new ApiKey("Basic", "Authorization", "header");
//  }

  @Bean
  public SecurityContext securityContext() {
    String protectedUrlPattern = DemoCacheEndpoint.PROTECTED_URL.stream()
        .map(o -> o.concat(".*"))
        .collect(Collectors.joining("|"));

    return SecurityContext.builder().securityReferences(defaultAuth())
        .operationSelector(os -> os.requestMappingPattern().matches(protectedUrlPattern)
        ).build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections.singletonList(new SecurityReference("Basic", authorizationScopes));
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Demo Cache Ne")
        .description("APIs for Demo cache with Redis.")
        .version("1.0")
        .termsOfServiceUrl("Free 2 use")
        .contact(new Contact("anho", "none", "an.ho@bkitsolution.com"))
        .license("free license")
        .licenseUrl("none")
        .build();
  }

  @Bean
  public Docket api(TypeResolver typeResolver) {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .securityContexts(Collections.singletonList(securityContext()))
//        .securitySchemes(Collections.singletonList(basicApiKey()))

//        .securitySchemes(Collections.singletonList(apiKey()))
        .additionalModels(
            typeResolver.resolve(ErrorDetail.class)
        )
        .select()
        .apis(RequestHandlerSelectors
            .basePackage("cache.demo.controllers")
//            .or(RequestHandlerSelectors
//                .basePackage("ibtexternal.test.controller"))
        )
        .paths(PathSelectors.any())
        .build();
  }

}
