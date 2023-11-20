package com.nlmk.adp.services;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.session.MapSessionRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SocketMessageSenderImpl implements SocketMessageSender {

    private final SimpMessagingTemplate template;
    private final SimpUserRegistry simpUserRegistry;
    private final MapSessionRepository sessionRepository;
    private final String CUSTOM_TOPIC = "/topic/hello";

    @Override
    public void send(String msg) {
        var users = simpUserRegistry
                .findSubscriptions(i -> i.getDestination().contains(CUSTOM_TOPIC))
                .stream()
                .map(SimpSubscription::getSession)
                .map(SimpSession::getUser)
                .map(SimpUser::getName)
                .toList();

        users.forEach(user ->
                template.convertAndSendToUser(user, CUSTOM_TOPIC, msg)
        );
    }

}
