package com.nlmk.adp.db.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

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


    public NotificationUserSuccessEntity() {
    }

    public NotificationUserSuccessEntity(UUID notificationId, String email, OffsetDateTime readAt) {
        this.primaryKey = new NotificationEmailPk(notificationId, email);
        this.readAt = readAt;
    }

    public void setNotification(NotificationEntity notification) {
        this.notification = notification;
        if (this.getPrimaryKey() == null) {
            this.setPrimaryKey(new NotificationEmailPk());
        }
        this.getPrimaryKey().setNotificationId(notification.getId());
    }
}
