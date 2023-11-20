package com.nlmk.adp.kafka.dispatcher;

import com.nlmk.adp.services.NotificationService;
import com.nlmk.adp.services.mapper.NotificationToDtoMapper;
import lombok.AllArgsConstructor;
import nlmk.l3.mesadp.DbUserNotificationVer0;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationDispatcherImpl implements NotificationsDispatcher {

    private final NotificationService notificationService;
    private final NotificationToDtoMapper notificationDtoMapper;

    @Override
    public void dispatch(DbUserNotificationVer0 notification) {
        var dto = notificationDtoMapper.mapDataToDto(notification);
        notificationService.send(dto);
    }

}