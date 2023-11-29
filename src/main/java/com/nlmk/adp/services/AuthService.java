package com.nlmk.adp.services;

import java.util.Set;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;

import com.nlmk.adp.config.ObjectMapperHelper;
import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.dto.KeyckloakUserDto;
import com.nlmk.adp.dto.SimpleStompAccount;
import com.nlmk.adp.dto.StompAuthenticationToken;
import com.nlmk.adp.dto.StompPrincipal;

/**
 * 123.
 */
@Component
public class AuthService implements AuthenticationManager {

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
    private StompAuthenticationToken createUserOrFail(JWT jwt) {
        KeyckloakUserDto user = ObjectMapperHelper.getObjectMapper()
                                                  .readValue(
                                                          ((SignedJWT) jwt).getPayload().toString(),
                                                          KeyckloakUserDto.class);
        Set<String> roles = user.getResourceAccess().getAccount().getRoles();

        if (user.getEmail() == null) {
            throw new OAuth2AuthenticationException("Access Denied. No user email found");
        }

        return new StompAuthenticationToken(
                null,
                new SimpleStompAccount(
                        new StompPrincipal(user.getEmail()),
                        roles)
        );
    }

}