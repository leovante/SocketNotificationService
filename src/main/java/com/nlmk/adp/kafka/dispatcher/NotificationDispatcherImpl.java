package com.nlmk.adp.kafka.dispatcher;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.nlmk.adp.services.NotificationService;
import com.nlmk.adp.services.mapper.NotificationToDtoMapper;
import nlmk.l3.mesadp.DbUserNotificationVer0;

/**
 * NotificationDispatcherImpl.
 */
@Service
@AllArgsConstructor
public class NotificationDispatcherImpl implements NotificationsDispatcher {

    private final NotificationService notificationService;

    private final NotificationToDtoMapper notificationDtoMapper;

    /**
     * dispatch.
     *
     * @param notification
     *         notification
     */
    @Override
    public void dispatch(DbUserNotificationVer0 notification) {
        var dto = notificationDtoMapper.mapDataToDto(notification);
        notificationService.send(dto);
    }

}