package com.nlmk.adp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.UUID;

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


    public NotificationRolesEntity() {
    }

    public NotificationRolesEntity(UUID notificationId, String role, String roleType) {
        this.primaryKey = new NotificationRolesPk(notificationId, role);
        this.roleType = roleType;
    }

    public void setNotification(NotificationEntity notification) {
        this.notification = notification;
        if (this.getPrimaryKey() == null) {
            this.setPrimaryKey(new NotificationRolesPk());
        }
        this.getPrimaryKey().setNotificationId(notification.getId());
    }
}
