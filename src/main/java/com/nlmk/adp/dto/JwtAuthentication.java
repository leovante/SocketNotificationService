package com.nlmk.adp.dto;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Setter
@Getter
@Accessors
public class JwtAuthentication extends AbstractAuthenticationToken {

    private String credentialsToken;

    private String user;

    private Collection<GrantedAuthority> authorities;


    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public JwtAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public String getCredentials() {
        return credentialsToken;
    }

    @Override
    public String getPrincipal() {
        return user;
    }

}
