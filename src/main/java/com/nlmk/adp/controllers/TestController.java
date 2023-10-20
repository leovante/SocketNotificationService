package com.nlmk.adp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Временный тестовый контроллер.
 */
@RestController
public class TestController {

    @GetMapping("/test")
    String test() {
        return "test";
    }

}
