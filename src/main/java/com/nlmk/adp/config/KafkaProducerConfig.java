package com.nlmk.adp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.nlmk.adp.commons.kafka.KafkaHttpProxyProducer;
import com.nlmk.adp.commons.kafka.config.KafkaRestProxyConfigurationProperties;
import com.nlmk.adp.commons.kafka.dto.MessageBatchMapper;
import com.nlmk.adp.kafka.producer.KafkaHttpProxyProducerV2Impl;

/**
 * 123.
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * 123.
     *
     * @param messageBatchMapper
     *         123.
     * @param kafkaRestProxyConfigurationProperties
     *         123.
     *
     * @return 123.
     */
    @Bean
    @Primary
    public KafkaHttpProxyProducer kafkaHttpProxyProducer(
            MessageBatchMapper messageBatchMapper,
            KafkaRestProxyConfigurationProperties kafkaRestProxyConfigurationProperties
    ) {
        return new KafkaHttpProxyProducerV2Impl(
                messageBatchMapper,
                kafkaRestProxyConfigurationProperties.getUrl(),
                kafkaRestProxyConfigurationProperties.getUsername(),
                kafkaRestProxyConfigurationProperties.getPassword()
        );
    }

}
