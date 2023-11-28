package com.nlmk.adp.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.session.MapSessionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketMessageSenderImpl implements SocketMessageSender {

    private final SimpMessagingTemplate template;
    private final SimpUserRegistry simpUserRegistry;
    private final MapSessionRepository sessionRepository;

    @Value("${websocket.topic:/topic/hello}")
    private String MAIN_TOPIC;

    @Override
    public void send(String msg) {
        var users = simpUserRegistry
                .findSubscriptions(i -> i.getDestination().contains(MAIN_TOPIC))
                .stream()
                .map(SimpSubscription::getSession)
                .map(SimpSession::getUser)
                .map(SimpUser::getName)
                .toList();

        users.forEach(user ->
                template.convertAndSendToUser(user, MAIN_TOPIC, msg)
        );
    }

}
