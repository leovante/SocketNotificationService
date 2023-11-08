package com.nlmk.adp.db.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "invalid_notifications")
@Data
public class InvalidNotificationsEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "created_at")
    private OffsetDateTime created_at;

    @Column(name = "updated_at")
    private OffsetDateTime updated_at;

    @Column(name = "raw_message")
    @Length(max = 4000)
    private String raw_message;

    @Column(name = "error_message")
    @Length(max = 100)
    private String error_message;

}
