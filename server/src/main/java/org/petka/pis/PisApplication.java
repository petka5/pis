package org.petka.pis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class PisApplication {

    /**
     * Spring boot main class.
     *
     * @param args application arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(PisApplication.class, args);
    }
}
