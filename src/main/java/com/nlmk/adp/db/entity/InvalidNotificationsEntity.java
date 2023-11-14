package com.nlmk.adp.db.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.nlmk.adp.db.entity.convert.RawMessageConverter;
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
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "raw_message")
    @Convert(converter = RawMessageConverter.class)
    private JsonNode rawMessage;

    @Column(name = "error_message")
    @Length(max = 8092)
    private String errorMessage;

}
