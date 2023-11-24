package com.nlmk.adp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.dto.KeyckloakUserDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthService implements AuthenticationManager {

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuthentication) {
            var jwt = JWTParser.parse(((JwtAuthentication) authentication).getCredentialsToken().split("Bearer ")[1]);
            var userToken = createUserOrFail(jwt);
            userToken.setAuthenticated(true);
            return userToken;
        } else {
            throw new RuntimeException("Unexpected auth type");
        }
    }

    @SneakyThrows
    private KeycloakAuthenticationToken createUserOrFail(JWT jwt) {
        KeyckloakUserDto user = mapper.readValue(((SignedJWT) jwt).getPayload().toString(), KeyckloakUserDto.class);
        Set<String> roles = user.getResource_access().getAccount().getRoles();

        return new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(new KeycloakPrincipal<>(user.getEmail(), null),
                        roles,
                        null),
                false);
    }

}