package com.nlmk.adp.dto;

import java.security.Principal;

import lombok.AllArgsConstructor;

/**
 * StompPrincipal.
 */
@AllArgsConstructor
public class StompPrincipal implements Principal {

    private String name;

    @Override
    public String getName() {
        return name;
    }

}
