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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.dto.KeyckloakUserDto;
import com.nlmk.adp.dto.SimpleStompAccount;
import com.nlmk.adp.dto.StompAuthenticationToken;
import com.nlmk.adp.dto.StompPrincipal;

/**
 * Аутентификация для веб сокетов.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthJwt implements AuthenticationManager {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtAuthentication auth) {
            log.info("auth for new ws user " + auth.getUser());
            var jwt = JWTParser.parse(auth.getCredentialsToken().split("Bearer ")[1]);
            var userToken = createUserOrFail(jwt);
            userToken.setAuthenticated(true);
            return userToken;
        } else {
            throw new RuntimeException("Unexpected auth type");
        }
    }

    @SneakyThrows
    private StompAuthenticationToken createUserOrFail(JWT jwt) {
        KeyckloakUserDto user = objectMapper.readValue(
                                                          ((SignedJWT) jwt).getPayload().toString(),
                                                          KeyckloakUserDto.class);
        Set<String> roles = user.getRealmAccess().getRoles();
        var authorities = roles.stream()
                               .map(i -> new SimpleGrantedAuthority(String.format("ROLE_%s", i)))
                               .toList();

        if (user.getEmail() == null) {
            throw new OAuth2AuthenticationException("Access Denied. No user email found");
        }

        return new StompAuthenticationToken(
                authorities,
                new SimpleStompAccount(
                        new StompPrincipal(user.getEmail()),
                        roles
                )
        );
    }

    /**
     * createKeyckloakUserDto.
     *
     * @return StompAuthenticationToken
     */
    @SneakyThrows
    public KeyckloakUserDto getKeyckloakUserDto() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            var jwt = JWTParser.parse(((JwtAuthenticationToken) authentication).getToken().getTokenValue());
            KeyckloakUserDto user = objectMapper.readValue(
                                                              ((SignedJWT) jwt).getPayload().toString(),
                                                              KeyckloakUserDto.class);

            if (user.getEmail() == null) {
                throw new OAuth2AuthenticationException("Access Denied. No user email found");
            }

            return user;

        } else {
            throw new RuntimeException("Unexpected auth type");
        }
    }

}
