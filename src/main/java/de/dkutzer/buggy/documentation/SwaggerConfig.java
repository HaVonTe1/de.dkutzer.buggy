package de.dkutzer.buggy.documentation;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import com.google.common.base.Predicate;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket serviceApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
        .groupName("buggy-service-api")
        .apiInfo(apiInfo())
        .useDefaultResponseMessages(false)
        .select()
        .paths(paths())
        .build();
  }

  private Predicate<String> paths() {
    return or(regex("/developers/.*"), regex("/issues/.*"), regex("/planning/.*"));
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("BUGGY Service API")
        .description(
            "The BUGGY Service provides endpoints for the Buggy System")
        .build();
  }
}
