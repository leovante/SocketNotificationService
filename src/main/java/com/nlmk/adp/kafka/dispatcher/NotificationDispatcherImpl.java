package com.nlmk.adp.kafka.dispatcher;

import com.nlmk.adp.dto.DbUserNotificationVer0;
import com.nlmk.adp.services.NotificationService;
import com.nlmk.adp.services.mapper.NotificationDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public class NotificationDispatcherImpl implements NotificationsDispatcher {

    private final NotificationService notificationService;
    private final NotificationDtoMapper notificationDtoMapper;

    @Override
    public void dispatch(DbUserNotificationVer0 notification) {
        var data = notification.getData();
        if (data.getBody() == null || data.getHeader() == null) {
            notificationService.invalidate(notification);
        }

        var dto = notificationDtoMapper.mapDataToDto(notification);
        notificationService.send(dto);
    }

}