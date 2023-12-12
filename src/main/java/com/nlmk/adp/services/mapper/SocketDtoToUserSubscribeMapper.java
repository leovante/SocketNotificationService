package com.nlmk.adp.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "happenedAt")
    NotificationBaseDto mapDtoToMessage(NotificationDto dto);

}
