package com.nlmk.adp.dto;

import java.security.Principal;

/**
 * временная обертка для работы с авторизацией веб сокета.
 */
public class StompPrincipal implements Principal {

    String name;

    /**
     * StompPrincipal.
     *
     * @param name
     *          name
     */
    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
