package de.dkutzer.buggy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication//(exclude = EmbeddedMongoAutoConfiguration.class)
@EnableSwagger2
public class BuggyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuggyApplication.class, args);
    }

}
