package org.petka.pis.configuration;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;

/**
 * Moving this configuration in separate file, because of circular dependency.
 */
//@Configuration
public class KeycloakConfigResolverConfig {

    /**
     * SpringBoot config resolver.
     *
     * @return KeycloakSpringBootConfigResolver
     */
    //@Bean
    public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}
