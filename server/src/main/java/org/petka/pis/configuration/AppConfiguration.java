package org.petka.pis.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.petka.pis.persistence.entities.BaseEntity;
import org.petka.pis.persistence.repositories.CustomRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Base application configurations.
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.petka.pis.persistence.repositories",
        repositoryBaseClass = CustomRepositoryImpl.class)
public class AppConfiguration {

    /**
     * ModelMapper bean.
     *
     * @return modelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }

    /**
     * Fields that must be omitted on patch.
     *
     * @return list of the fields
     */
    @Bean
    public List<String> nonPatchableFields() {

        return List.of(Arrays.stream(BaseEntity.class.getDeclaredFields())
                               .map(Field::getName).toArray(String[]::new));

    }
}
