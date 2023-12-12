package com.nlmk.adp.services.mapper;

import org.mapstruct.Mapper;

import com.nlmk.adp.kafka.dto.NotificationBaseDto;
import com.nlmk.adp.kafka.dto.NotificationDto;
import com.nlmk.adp.util.SpringMapperConfig;

/**
 * NotificationToDtoMapper.
 */
@Mapper(config = SpringMapperConfig.class, uses = {KafkaDateMapper.class})
public interface SocketDtoToUserSubscribeMapper {

    /**
     * mapDtoToMessage.
     *
     * @param dto dto
     * @return UserSubscribeMessageDto
     */
    NotificationBaseDto mapDtoToMessage(NotificationDto dto);

}
