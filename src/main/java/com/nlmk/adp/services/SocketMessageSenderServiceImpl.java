package com.nlmk.adp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

/**
 * SocketMessageSenderImpl.
 */
@Service
@RequiredArgsConstructor
public class SocketMessageSenderServiceImpl implements SocketMessageSenderService {

    private final SimpMessagingTemplate template;
    private final SimpUserRegistry simpUserRegistry;
    private final SessionRepository<MapSession> sessionRepository;

    @Value("${websocket.topic.start:/topic/hello}")
    private String START_TOPIC;

    @Override
    public void send(String msg) {
        var users = simpUserRegistry
                .findSubscriptions(i -> i.getDestination().contains(START_TOPIC))
                .stream()
                .map(SimpSubscription::getSession)
                .map(SimpSession::getUser)
                .map(SimpUser::getName)
                .distinct()
                .toList();

        users.forEach(user ->
                template.convertAndSendToUser(user, START_TOPIC, msg)
        );
    }

}
