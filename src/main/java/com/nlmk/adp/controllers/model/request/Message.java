package com.nlmk.adp.controllers.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Пейлоад сообщений веб сокета.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Message {

    private String name;
    private Status status;

}
