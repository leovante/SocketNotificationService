package com.nlmk.adp.services.component;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * PrincipalJwt.
 */
@Component
@RequestScope
public class PrincipalJwt {

    /**
     * getName.
     *
     * @return String
     */
    public String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * getAuthentication.
     *
     * @return String
     */
    public Set<String> getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(i -> i.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());
    }

}
