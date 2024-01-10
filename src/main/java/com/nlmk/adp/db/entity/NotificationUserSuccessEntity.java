package com.nlmk.adp.db.entity;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

/**
 * Сущности доставки уведомления пользователю..
 */
@Setter
@Getter
@Entity
@Table(name = "notification_user_success")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class NotificationUserSuccessEntity {

    @EmbeddedId
    private NotificationEmailPk primaryKey;

    @Nullable
    @Column(name = "read_at")
    private Instant readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "notificationId")
    private NotificationEntity notification;

    /**
     * setNotification.
     *
     * @param notification
     *         notification
     */
    public void setNotification(NotificationEntity notification) {
        this.notification = notification;
        if (this.getPrimaryKey() == null) {
            this.setPrimaryKey(new NotificationEmailPk());
        }
        this.getPrimaryKey().setNotificationId(notification.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        NotificationUserSuccessEntity task = (NotificationUserSuccessEntity) o;
        return primaryKey.getNotificationId() != null
                && Objects.equals(primaryKey.getNotificationId(), task.primaryKey.getNotificationId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
