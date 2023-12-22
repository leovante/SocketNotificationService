package com.nlmk.adp.services.component;

import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.nlmk.adp.dto.JwtAuthentication;

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
        return ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getRoles();
    }

}
