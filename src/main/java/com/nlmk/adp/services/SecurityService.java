package com.nlmk.adp.services;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SecurityService {
    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class
    public KeycloakAuthenticationToken getAuthenticatedOrFail(final String  token) throws AuthenticationException {
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
        return new KeycloakAuthenticationToken(
                null,
                false,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}