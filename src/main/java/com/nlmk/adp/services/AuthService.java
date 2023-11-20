package com.nlmk.adp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlmk.adp.dto.KeyckloakUserDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final ObjectMapper mapper;
    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class

    @SneakyThrows
    public KeycloakAuthenticationToken getAuthenticatedOrFail(final String token) throws AuthenticationException {
//        if (username == null || username.trim().isEmpty()) {
//            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
//        }
//        if (password == null || password.trim().isEmpty()) {
//            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
//        }
        // Add your own logic for retrieving user in fetchUserFromDb()
//        if (fetchUserFromDb(username, password) == null) {
//            throw new BadCredentialsException("Bad credentials for user " + username);
//        }

        // null credentials, we do not pass the password along

        var decoder = Base64.getDecoder();
        String[] chunks = token.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        KeyckloakUserDto user = mapper.readValue(payload, KeyckloakUserDto.class);
        Set<String> roles = user.getResource_access().getAccount().getRoles();

        return new KeycloakAuthenticationToken(
                new SimpleKeycloakAccount(new KeycloakPrincipal<>(user.getScope(), null),
                        roles,
                        null),
                false);
    }
}