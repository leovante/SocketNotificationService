package com.nlmk.adp.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationsServiceImpl implements NotificationsService {

    private final SocketMessageSender socketMessageSender;

    @Override
    public void dispatch(String msg) {
        socketMessageSender.send(msg);
    }

}