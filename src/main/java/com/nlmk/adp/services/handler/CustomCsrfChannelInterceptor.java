package com.nlmk.adp.services.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * отключалка csdf для спринга.
 */
@Component("csrfChannelInterceptor")
public class CustomCsrfChannelInterceptor implements ChannelInterceptor {

    /**
     * preSend.
     *
     * @param message message
     * @param channel channel
     * @return Message
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message;
    }

}
