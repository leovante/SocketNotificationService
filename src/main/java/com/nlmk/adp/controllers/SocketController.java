package com.nlmk.adp.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.nlmk.adp.controllers.model.request.Message;
import com.nlmk.adp.controllers.model.response.Greeting;

/**
 * временный контроллер для дебага веб сокета.
 */
@Controller
public class SocketController {

    /**
     * greeting.
     *
     * @param message
     *          message.
     *
     * @return Greeting
     * @throws Exception
     *          Exception.
     */
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}
