package com.nlmk.adp.controllers.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Сообщение об ошибке.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private String message;

}
