package com.nlmk.adp.db.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Embeddable
@ToString(exclude = "notificationId")
public class NotificationEmailPk implements Serializable {

    @Column(name = "notification_id")
    @EqualsAndHashCode.Exclude
    private UUID notificationId;

    @Column(name = "email")
    @Length(max = 100)
    private String email;


    public NotificationEmailPk (){}

    public NotificationEmailPk(UUID notificationId, String email) {
        this.notificationId = notificationId;
        this.email = email;
    }
}
