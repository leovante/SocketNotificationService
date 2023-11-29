package com.nlmk.adp.dto;

import java.security.Principal;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * SimpleStompAccount.
 */
@Setter
@Getter
@AllArgsConstructor
public class SimpleStompAccount {

    private Principal principal;
    private Set<String> roles;

}
