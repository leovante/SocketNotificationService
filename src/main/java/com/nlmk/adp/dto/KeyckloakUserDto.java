package com.nlmk.adp.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("realm_access")
    private RealmAccess realmAccess;

    /**
     * 123.
     */
    @Data
    @Getter
    public static class RealmAccess {

        private Set<String> roles;

    }

}
