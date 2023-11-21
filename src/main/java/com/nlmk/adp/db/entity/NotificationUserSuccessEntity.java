package com.nlmk.adp.db.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * NotificationUserSuccessEntity.
 */
@Setter
@Getter
@Entity
@Table(name = "notification_user_success")
public class NotificationUserSuccessEntity {

    @EmbeddedId
    private NotificationEmailPk primaryKey;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @MapsId(value = "id")
    @ManyToOne
    private NotificationEntity notification;

    /**
     * NotificationUserSuccessEntity.
     */
    public NotificationUserSuccessEntity() {
    }

    /**
     * NotificationUserSuccessEntity.
     *
     * @param  notificationId notificationId
     * @param  email email
     * @param  readAt readAt
     */
    public NotificationUserSuccessEntity(UUID notificationId, String email, OffsetDateTime readAt) {
        this.primaryKey = new NotificationEmailPk(notificationId, email);
        this.readAt = readAt;
    }

    /**
     * setNotification.
     *
     * @param notification notification
     */
    public void setNotification(NotificationEntity notification) {
        this.notification = notification;
        if (this.getPrimaryKey() == null) {
            this.setPrimaryKey(new NotificationEmailPk());
        }
        this.getPrimaryKey().setNotificationId(notification.getId());
    }

}
