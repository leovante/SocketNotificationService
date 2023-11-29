package com.nlmk.adp.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyckloakUserDto {
    private String scope;
    private String email;
    private ResourceAccess resource_access;

    @Data
    @Getter
    public static class ResourceAccess {
        private Account account;
    }

    @Data
    @Getter
    public static class Account {
        private Set<String> roles;
    }
}
