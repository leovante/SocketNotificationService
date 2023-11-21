package com.nlmk.adp.db.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * NotificationRolesEntity.
 */
@Setter
@Getter
@Entity
@Table(name = "notification_roles")
public class NotificationRolesEntity {

    @EmbeddedId
    private NotificationRolesPk primaryKey;

    @Column(name = "role_type")
    @Length(max = 100)
    private String roleType;

    @MapsId(value = "id")
    @ManyToOne
    private NotificationEntity notification;

    /**
     * NotificationRolesEntity.
     */
    public NotificationRolesEntity() {
    }

    /**
     * NotificationRolesEntity.
     *
     * @param notificationId notificationId
     * @param role role
     * @param roleType roleType
     */
    public NotificationRolesEntity(UUID notificationId, String role, String roleType) {
        this.primaryKey = new NotificationRolesPk(notificationId, role);
        this.roleType = roleType;
    }

    /**
     * setNotification.
     *
     * @param notification notification
     */
    public void setNotification(NotificationEntity notification) {
        this.notification = notification;
        if (this.getPrimaryKey() == null) {
            this.setPrimaryKey(new NotificationRolesPk());
        }
        this.getPrimaryKey().setNotificationId(notification.getId());
    }

}
