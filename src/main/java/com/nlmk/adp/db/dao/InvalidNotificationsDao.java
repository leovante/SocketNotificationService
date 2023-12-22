package com.nlmk.adp.db.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;
import com.nlmk.adp.db.repository.InvalidNotificationsRepository;

/**
 * Для работы с невалидными уведомлениями.
 */
@Service
@AllArgsConstructor
public class InvalidNotificationsDao {

    private final InvalidNotificationsRepository repository;

    private final ObjectMapper objectMapper;

    /**
     * save.
     *
     * @param body
     *         body
     * @param reason
     *         причина, по которой уведомление ошибочно.
     *
     * @return InvalidNotificationsEntity
     */
    @Transactional
    public InvalidNotificationsEntity saveNew(
            Object body,
            String reason
    ) {
        var msg = objectMapper.valueToTree(body);
        var entity = new InvalidNotificationsEntity();
        entity.setRawMessage(msg);
        entity.setErrorMessage(reason);
        return repository.save(entity);
    }

}
