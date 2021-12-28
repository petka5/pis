package org.petka.pis.configuration;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.JsonWebToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class AuthAspect {

    private static final String ORG_ID = "orgId";
    private static final String ROLE_PREFIX = "ROLE_";

    /**
     * Intercept all rest endpoint.
     */
    @Pointcut("execution(* org.petka.pis.delegators.*.*(..))")
    public void executeController() {
        // Match all controller delegates.
    }

    /**
     * Check of user is organization, id from token match to the id in the path.
     */
    @Before("executeController()")
    public void checkOrg() {

        if (!isOperatorUser()) {
            UUID orgIdFromToken = getOrgIdFromToken();
            UUID orgIdFromPath = getOrgIdFromPath();

            if (Objects.nonNull(orgIdFromPath) && !Objects.equals(orgIdFromPath, orgIdFromToken)) {
                log.warn("Org in path {} doesn't match org in the token {}", orgIdFromPath, orgIdFromToken);
                throw new AccessDeniedException("OrgId mismatch");
            }
        }
    }

    private boolean isOperatorUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLE_PREFIX.concat(Role.OPERATOR.getName())::equals);
    }

    private UUID getOrgIdFromToken() {

        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .map(KeycloakPrincipal.class::cast)
                .map(KeycloakPrincipal::getKeycloakSecurityContext)
                .map(KeycloakSecurityContext::getToken)
                .map(JsonWebToken::getOtherClaims)
                .filter(e -> e.containsKey(ORG_ID))
                .map(e -> e.get(ORG_ID))
                .map(String.class::cast)
                .map(UUID::fromString)
                .orElse(null);
    }

    private UUID getOrgIdFromPath() {
        return Optional.of(RequestContextHolder.currentRequestAttributes())
                .map(e -> e.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,
                                         RequestAttributes.SCOPE_REQUEST))
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(e -> e.get(ORG_ID))
                .map(String.class::cast)
                .map(UUID::fromString)
                .orElse(null);
    }
}
