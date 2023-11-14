package com.nlmk.adp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Список констант с эррор мессаджами.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessagesList {

    BODY_INVALID("body should not be empty"),
    HEADER_INVALID("header should not be empty"),

    ROLES_MISMATCH("acceptRoles and rejectRoles should not cross"),
    EMPTY_EMAILS_OR_ROLES("emails or acceptRoles should not be empty"),
    EMAILS_INVALID("invalid emails"),

    HREF_DOMAIN_INVALID("href domain should be nlmk.com")
    ;

    private final String value;

}
