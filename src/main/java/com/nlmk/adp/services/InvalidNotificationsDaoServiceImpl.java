package com.nlmk.adp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.nlmk.adp.db.entity.InvalidNotificationsEntity;
import com.nlmk.adp.db.repository.InvalidNotificationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class InvalidNotificationsDaoServiceImpl implements InvalidNotificationsDaoService{

    private final InvalidNotificationsRepository repository;

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
