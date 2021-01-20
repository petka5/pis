package org.petka.pis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
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
