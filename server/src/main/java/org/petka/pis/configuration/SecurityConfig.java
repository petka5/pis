package org.petka.pis.configuration;

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

//https://www.baeldung.com/spring-boot-keycloak

/**
 * KeyCloak security configuration.
 */
//@Configuration
//@EnableWebSecurity
//@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig {

    /**
     * Global configuration.
     *
     * @param auth AuthenticationManagerBuilder
     */
    //@Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) {

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = new KeycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    /**
     * sessionAuthenticationStrategy.
     *
     * @return return.
     */
    //@Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * configure.
     *
     * @param http http param
     * @return SecurityFilterChain
     */
    //@Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.servletApi().rolePrefix("");
        http.csrf().disable().exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());

        http.authorizeHttpRequests().requestMatchers("/api.yaml").permitAll()
                .requestMatchers("/api/operator/**", "/api/operator**").hasAnyRole(Role.OPERATOR.getName())
                .requestMatchers("/api/**", "/api**").hasAnyRole(Role.ORGANIZATION.getName(), Role.OPERATOR.getName())
                .anyRequest().permitAll();
        return http.build();
    }
}
