package com.nlmk.adp.db.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * Составной ид для роли уведомления.
 */
@Setter
@Getter
@Embeddable
@ToString(exclude = "notificationId")
@NoArgsConstructor
public class NotificationRolesPk implements Serializable {

    @Column(name = "notification_id")
    @EqualsAndHashCode.Exclude
    private UUID notificationId;

    @Column(name = "role")
    @Length(max = 100)
    private String role;

}
