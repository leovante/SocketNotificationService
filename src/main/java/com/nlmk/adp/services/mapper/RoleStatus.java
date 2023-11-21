package com.nlmk.adp.services.mapper;

/**
 * RoleStatus.
 */
public enum RoleStatus {

    ACCEPT("accept"),
    REJECT("reject");

    private final String value;

    RoleStatus(String value) {
        this.value = value;
    }

    /**
     * toString.
     *
     * @return String
     */
    public String toString() {
        return value;
    }

}
