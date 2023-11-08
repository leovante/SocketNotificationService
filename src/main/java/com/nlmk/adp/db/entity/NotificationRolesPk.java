package com.nlmk.adp.db.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@ToString(exclude = "notificationId")
public class NotificationRolesPk implements Serializable {

    @Column(name = "notification_id")
    @EqualsAndHashCode.Exclude
    private UUID notificationId;

    @Column(name = "role")
    @Length(max = 100)
    private String role;


    public NotificationRolesPk() {
    }

    public NotificationRolesPk(UUID notificationId, String role) {
        this.notificationId = notificationId;
        this.role = role;
    }
}
