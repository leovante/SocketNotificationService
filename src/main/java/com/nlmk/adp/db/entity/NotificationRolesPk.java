package com.nlmk.adp.db.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * NotificationRolesPk.
 */
@Setter
@Getter
@Embeddable
@ToString(exclude = "notificationId")
public class NotificationRolesPk implements Serializable {

    @Column(name = "notification_id")
    @EqualsAndHashCode.Exclude
    private UUID notificationId;

    @Column(name = "role")
    @Length(max = 100)
    private String role;

    /**
     * NotificationRolesPk.
     */
    public NotificationRolesPk() {
    }

    /**
     * NotificationRolesPk.
     *
     * @param notificationId
     *         notificationId
     * @param role
     *         role
     */
    public NotificationRolesPk(UUID notificationId, String role) {
        this.notificationId = notificationId;
        this.role = role;
    }

}
