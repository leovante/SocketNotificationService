package com.nlmk.adp.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 123.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyckloakUserDto {

    private String scope;

    private String email;

    private ResourceAccess resourceAccess;

    /**
     * 123.
     */
    @Data
    @Getter
    public static class ResourceAccess {

        private Account account;

    }

    /**
     * 123.
     */
    @Data
    @Getter
    public static class Account {

        private Set<String> roles;

    }

}
