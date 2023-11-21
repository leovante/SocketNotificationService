package com.nlmk.adp.services;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.session.MapSessionRepository;
import org.springframework.stereotype.Service;

/**
 * SocketMessageSenderImpl.
 */
@Service
@AllArgsConstructor
public class SocketMessageSenderImpl implements SocketMessageSenderService {

    private final SimpMessagingTemplate template;
    private final SimpUserRegistry simpUserRegistry;
    private final MapSessionRepository sessionRepository;

    @Override
    public void send(String msg) {
        /* var users = simpUserRegistry
                .getUsers()
                .stream()
                .map(SimpUser::getName)
                .collect(Collectors.toList());*/

        var users = simpUserRegistry
                .findSubscriptions(i -> i.getDestination().contains("/topic/greetings"))
                .stream()
                .map(SimpSubscription::getSession)
                .map(SimpSession::getUser)
                .map(SimpUser::getName)
                .toList();

        template.convertAndSendToUser("sessionRegistry.getAllPrincipals().get(0)", "/topic/hello", msg);
        template.convertAndSend("/topic/hello", msg);
    }

}