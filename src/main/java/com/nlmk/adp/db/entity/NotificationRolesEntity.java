package com.nlmk.adp.db.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.nlmk.adp.services.mapper.NotificationRoleType;

/**
 * Роль уведомления.
 */
@Setter
@Getter
@Entity
@Table(name = "notification_roles")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class NotificationRolesEntity {

    @EmbeddedId
    private NotificationRolesPk primaryKey;

    @Column(name = "role_type")
    @Enumerated(value = EnumType.STRING)
    private NotificationRoleType roleType;

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
            this.setPrimaryKey(new NotificationRolesPk());
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

        NotificationRolesEntity task = (NotificationRolesEntity) o;
        return primaryKey.getNotificationId() != null
                && Objects.equals(primaryKey.getNotificationId(), task.primaryKey.getNotificationId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
