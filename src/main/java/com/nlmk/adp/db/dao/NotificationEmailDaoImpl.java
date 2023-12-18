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
 * NotificationEmailDaoImpl.
 */
@Service
@RequiredArgsConstructor
public class NotificationEmailDaoImpl implements NotificationEmailDao {

    private final NotificationEmailRepository notificationEmailRepository;

    private final NotificationRepository notificationRepository;

    /**
     * markAllReadedByEmail.
     *
     * @param email
     *         email
     * @param roles
     *         roles
     * @param uuids
     *         uuids
     *
     * @return List
     */
    @Override
    @Transactional
    public List<NotificationUserSuccessEntity> markAllReadedByEmail(String email, Set<String> roles, Set<UUID> uuids) {
        var snapshots = notificationEmailRepository
                .findUnreadMessagesByUuid(uuids, email, roles, 100);
        snapshots.forEach(i -> i.setReadAt(Instant.now()));

        var successEntity = notificationEmailRepository.saveAllAndFlush(snapshots);

        var successUuids = successEntity.stream()
                                        .map(i -> i.getPrimaryKey().getNotificationId())
                                        .collect(Collectors.toSet());
        var skippedEntity = uuids.stream()
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
