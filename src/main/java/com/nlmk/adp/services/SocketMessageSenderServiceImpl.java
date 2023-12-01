package com.nlmk.adp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.nlmk.adp.services.component.ActiveUserStore;

/**
 * SocketMessageSenderImpl.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SocketMessageSenderServiceImpl implements SocketMessageSenderService {

    @Value("${websocket.topic.start:/topic/hello}")
    private String startTopic;

    private final SimpMessagingTemplate template;

    private final ActiveUserStore activeUserStore;

    /**
     * send.
     *
     * @param msg msg
     */
    @Override
    public void send(String msg) {
        log.info("Send for users message: {}, topic: {}", msg, startTopic);

        activeUserStore.getUsers().forEach(user -> {
                    template.convertAndSendToUser(user.getUser().getName(), startTopic, msg);
                }
        );
    }

}
