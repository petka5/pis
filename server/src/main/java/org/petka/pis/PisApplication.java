package org.petka.pis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PisApplication {

    /**
     * Spring boot main class.
     * https://stackoverflow.com/questions/25738569/how-to-map-a-map-json-column-to-java-object-with-jpa
     * @param args application arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(PisApplication.class, args);
    }
}
