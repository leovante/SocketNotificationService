package com.nlmk.adp.services.mapper;

public enum RoleStatus {
    ACCEPT("accept"),
    REJECT("reject");

    RoleStatus(String value) {
        this.value = value;
    }

    private String value;

    public String toString() {
        return value;
    }
}
