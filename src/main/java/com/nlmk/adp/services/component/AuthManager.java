package com.nlmk.adp.services.component;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.dto.KeyckloakUserDto;

/**
 * Общий менеджер аутентификации.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthManager implements AuthenticationManager {

    /**
     * Префикс роли.
     *
     * @see SecurityExpressionRoot.defaultRolePrefix префикс роли.
     */
    public static final String DEFAULT_ROLE_PREFIX = "ROLE_";

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof BearerTokenAuthenticationToken bearerJwt) {
            String jwtString = bearerJwt.getToken().replace("Bearer ", "");
            var jwt = JWTParser.parse(jwtString);
            var userToken = buildUserToken(jwt, jwtString);
            userToken.setAuthenticated(true);
            return userToken;
        } else {
            throw new RuntimeException("Unexpected auth type");
        }
    }

    @SneakyThrows
    private JwtAuthentication buildUserToken(JWT jwt, String jwtString) {
        KeyckloakUserDto user = objectMapper.readValue(
                ((SignedJWT) jwt).getPayload().toString(),
                KeyckloakUserDto.class);

        Set<String> roles = user.getRealmAccess().getRoles();
        var authorities = roles.stream()
                               .filter(it -> !it.isBlank())
                               .map(i -> new SimpleGrantedAuthority(DEFAULT_ROLE_PREFIX + i))
                               .toList();

        if (user.getEmail() == null) {
            throw new OAuth2AuthenticationException("Access Denied. No user email found");
        }

        return new JwtAuthentication(
                authorities,
                roles,
                user.getEmail(),
                jwtString
        );
    }

}
