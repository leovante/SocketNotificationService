package com.nlmk.adp.dto;

import java.util.Collection;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * JwtAuthentication.
 */
@Setter
@Getter
@Accessors
public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String credentialsToken;

    private final String userEmailIdentity;

    private final Set<String> roles;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities
     *         the collection of <tt>GrantedAuthority</tt>s for the principal represented by this authentication
     *         object.
     */
    public JwtAuthentication(
            Collection<? extends GrantedAuthority> authorities,
            Set<String> roles,
            String userEmailIdentity,
            String credentialsToken
    ) {
        super(authorities);
        this.roles = roles;
        this.userEmailIdentity = userEmailIdentity;
        this.credentialsToken = credentialsToken;
    }

    @Override
    public String getCredentials() {
        return credentialsToken;
    }

    @Override
    public String getPrincipal() {
        return userEmailIdentity;
    }

    @Override
    public String getName() {
        return userEmailIdentity;
    }

}
