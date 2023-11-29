package com.nlmk.adp.dto;

import java.security.Principal;
import java.util.Collection;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class StompAuthenticationToken extends AbstractAuthenticationToken implements Authentication {

    private SimpleStompAccount account;

    public StompAuthenticationToken(Collection<? extends GrantedAuthority> authorities, SimpleStompAccount simpleStompAccount) {
        super(authorities);
        account = simpleStompAccount;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Principal getPrincipal() {
        return account.getPrincipal();
    }

}
