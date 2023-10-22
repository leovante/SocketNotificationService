package com.nlmk.adp.config.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для перенаправления на документацию.
 */
@Controller
public class HomeController {

    /**
     * Перенаправляет на документацию.
     *
     * @return
     *         Редирект.
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping("/")
    public String index() {
        return "redirect:swagger-ui/";
    }

}