package com.nlmk.adp.services;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;
import com.nlmk.adp.db.repository.InvalidNotificationsRepository;

/**
 * InvalidNotificationsDaoServiceImpl.
 */
@Service
@AllArgsConstructor
public class InvalidNotificationsDaoServiceImpl implements InvalidNotificationsDaoService {

    private final InvalidNotificationsRepository repository;

    /**
     * save.
     *
     * @param msg msg
     * @param reason reason
     * @return InvalidNotificationsEntity
     */
    @Override
    @Transactional
    public InvalidNotificationsEntity save(JsonNode msg, String reason) {
        var entity = new InvalidNotificationsEntity();
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setRawMessage(msg);
        entity.setErrorMessage(reason);
        return repository.save(entity);
    }

}
