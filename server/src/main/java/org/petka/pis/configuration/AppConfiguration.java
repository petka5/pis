package org.petka.pis.configuration;

import org.modelmapper.ModelMapper;
import org.petka.pis.persistence.repositories.BaseRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Base application configurations.
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.petka.pis.persistence.repositories",
        repositoryBaseClass = BaseRepositoryImpl.class)
public class AppConfiguration {

    /**
     * ModelMapper bean.
     *
     * @return modelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
