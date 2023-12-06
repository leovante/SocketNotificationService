package com.nlmk.adp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.nlmk.adp.commons.kafka.KafkaHttpProxyProducer;
import com.nlmk.adp.commons.kafka.config.KafkaRestProxyConfigurationProperties;
import com.nlmk.adp.commons.kafka.dto.MessageBatchMapper;
import com.nlmk.adp.kafka.producer.KafkaHttpProxyProducerV2Impl;

/**
 * Копия конфигурации KafkaRestProxyAutoConfiguration. Оригинальная библиотека бинарно несовместима из-за мажорных
 * различий в версиях spring и java.
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * Клиент для kafka rest.
     *
     * @param messageBatchMapper
     *         конвертер.
     * @param kafkaRestProxyConfigurationProperties
     *         параметры.
     *
     * @return клиент.
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
