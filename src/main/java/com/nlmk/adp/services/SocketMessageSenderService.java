package com.nlmk.adp.services;

import com.nlmk.adp.dto.JwtAuthentication;
import com.nlmk.adp.kafka.dto.NotificationDto;

/**
 * SocketMessageSenderService.
 */
public interface SocketMessageSenderService {

    /**
     * send.
     *
     * @param msg
     *         msg
     */
    void send(NotificationDto msg);

    /**
     * resendToNotReadedEmails.
     *
     * @param user
     *         user
     */
    void resendToNotReadedWsUsers(JwtAuthentication user);

}
