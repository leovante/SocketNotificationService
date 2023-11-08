package com.nlmk.adp.services;

import com.nlmk.adp.db.entity.InvalidNotificationsEntity;
import com.nlmk.adp.db.repository.InvalidNotificationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class InvalidNotificationsDaoServiceImpl implements InvalidNotificationsDaoService{

    private final InvalidNotificationsRepository repository;

    @Override
    public InvalidNotificationsEntity save(String msg) {
        var entity = new InvalidNotificationsEntity();
        entity.setCreated_at(OffsetDateTime.now());
        entity.setRaw_message(msg);
        return repository.save(entity);
    }

}
