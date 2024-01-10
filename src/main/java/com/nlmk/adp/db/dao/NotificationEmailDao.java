package com.nlmk.adp.db.dao;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.entity.NotificationUserSuccessEntity;
import com.nlmk.adp.db.repository.NotificationEmailRepository;
import com.nlmk.adp.db.repository.NotificationRepository;

/**
 * Для работы с фактами прочтения уведомлений пользователями..
 */
@Service
@RequiredArgsConstructor
public class NotificationEmailDao {

    private final NotificationEmailRepository notificationEmailRepository;

    private final NotificationRepository notificationRepository;

    /**
     * Пометить уведомления прочитанными пользователем.
     *
     * @param email
     *         email, идентифицирующий пользователя.
     * @param roles
     *         роли пользователя.
     * @param notificationIds
     *         глобальные идентификаторы уведомлений.
     *
     * @return List
     */
    @Transactional
    public List<NotificationUserSuccessEntity> markNotificationsAsReadByUser(
            String email,
            Set<String> roles,
            Set<UUID> notificationIds
    ) {
        var snapshots = notificationEmailRepository.findUnreadMessagesByUuid(
                notificationIds,
                email,
                roles,
                100
        );
        snapshots.forEach(i -> i.setReadAt(Instant.now()));

        var successEntity = notificationEmailRepository.saveAllAndFlush(snapshots);

        var successUuids = successEntity.stream()
                                        .map(i -> i.getPrimaryKey().getNotificationId())
                                        .collect(Collectors.toSet());
        var skippedEntity = notificationIds.stream()
                                           .filter(i -> !successUuids.contains(i))
                                           .map(i -> notificationRepository.findById(i).get())
                                           .map(i -> {
                                               var newUser = new NotificationUserSuccessEntity();
                                               newUser.setNotification(i);
                                               newUser.getPrimaryKey().setEmail(email);
                                               newUser.setReadAt(Instant.now());
                                               return newUser;
                                           })
                                           .toList();

        return notificationEmailRepository.saveAllAndFlush(skippedEntity);
    }

}
