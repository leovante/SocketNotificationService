package com.nlmk.adp.services.component;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

/**
 * Служебный компонент для хранения вебсокет-сессий.
 */
@Component
@RequiredArgsConstructor
public class ActiveUserStore {

    @Value("${websocket.topic.start:/topic/notification}")
    private String startTopic;

    private final SimpUserRegistry simpUserRegistry;

    /**
     * getUserNames.
     *
     * @return List
     */
    public List<String> getUserNames() {
        return simpUserRegistry
                .findSubscriptions(i -> i.getDestination().contains(startTopic))
                .stream()
                .map(SimpSubscription::getSession)
                .map(SimpSession::getUser)
                .map(SimpUser::getName)
                .distinct()
                .toList();
    }

    /**
     * getUsers.
     *
     * @return List
     */
    public List<SimpSession> getUsersByTopic(String topic) {
        return simpUserRegistry
                .findSubscriptions(i -> i.getDestination().contains(topic))
                .stream()
                .map(SimpSubscription::getSession)
                .collect(Collectors.toList());
    }

}
