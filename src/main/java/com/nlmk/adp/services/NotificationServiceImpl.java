package com.nlmk.adp.services;

import com.nlmk.adp.config.ObjectMapperHelper;
import com.nlmk.adp.dto.DbUserNotificationVer0;
import com.nlmk.adp.kafka.dto.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDaoService notificationDaoService;
    private final InvalidNotificationsDaoService invalidNotificationsDaoService;
    private final SocketMessageSenderService socketMessageSenderService;

    @Override
    public void send(NotificationDto body) {

        notificationDaoService.save(body);
        socketMessageSenderService.send(body.href());

    }

    @Override
    public void invalidate(DbUserNotificationVer0 body, String reason) {
        var snapshot = ObjectMapperHelper.getObjectMapper().valueToTree(body);
        invalidNotificationsDaoService.save(snapshot, reason);
    }

}
