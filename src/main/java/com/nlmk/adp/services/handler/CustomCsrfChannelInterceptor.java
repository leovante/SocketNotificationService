package com.nlmk.adp.services.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * Отключение csrf для вебсокетов.
 */
@Component("csrfChannelInterceptor")
public class CustomCsrfChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message;
    }

}
