package com.nlmk.adp.kafka.producer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import com.nlmk.adp.commons.kafka.KafkaHttpProxyProducer;
import com.nlmk.adp.commons.kafka.dto.KafkaRestProxyResponse;
import com.nlmk.adp.commons.kafka.dto.MessageBatchMapper;
import com.nlmk.adp.commons.kafka.dto.MessagesBatchDto;

/**
 * Форк KafkaHttpProxyProducerImpl, необходим из-за проблем с версиями java и spring.
 */
@Slf4j
public class KafkaHttpProxyProducerV2Impl implements KafkaHttpProxyProducer {

    private static final String TOPICS = "/topics/";

    private static final String CONTENT = "application/vnd.kafka.avro.v2+json";

    private final WebClient kafkaWebClient;

    private final MessageBatchMapper messageBatchMapper;

    /**
     * Конструктор.
     */
    public KafkaHttpProxyProducerV2Impl(
            MessageBatchMapper messageBatchMapper,
            String url,
            String username,
            String password
    ) {
        this.messageBatchMapper = messageBatchMapper;

        // Этот WebClient приватный для KafkaRestProxy - его никто не перегрузит
        this.kafkaWebClient = WebClient
                .builder()
                .defaultHeaders(header -> {
                    if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
                        log.info("Unknown login-password, authorization headers not configured");
                    } else {
                        header.setBasicAuth(username, password);
                    }
                })
                .baseUrl(url + TOPICS)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT)
                .build();
    }

    /**
     * Отправка в топик.
     */
    @Override
    public <T extends SpecificRecordBase> Mono<KafkaRestProxyResponse> send(
            String topic,
            String key,
            List<T> messages
    ) {
        return send(
                topic,
                () -> CollectionUtils.isEmpty(messages),
                () -> messageBatchMapper.buildBatchFromRecords(key, messages)
        );
    }

    /**
     * Отправка в топик.
     */
    @Override
    public <T extends SpecificRecordBase> Mono<KafkaRestProxyResponse> send(
            String topic,
            Map<String, T> messages
    ) {
        return send(
                topic,
                () -> CollectionUtils.isEmpty(messages),
                () -> messageBatchMapper.buildBatchFromRecords(messages)
        );
    }

    private Mono<KafkaRestProxyResponse> send(
            String topic,
            BooleanSupplier isEmpty,
            Supplier<MessagesBatchDto> map
    ) {
        Objects.requireNonNull(topic, "Topic must be set!");

        if (Boolean.TRUE.equals(isEmpty.getAsBoolean())) {
            return Mono.error(new IllegalArgumentException("Empty message collection"));
        }

        MessagesBatchDto messagesBatchDto = map.get();

        return kafkaWebClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(topic).build())
                .body(Mono.just(messagesBatchDto), MessagesBatchDto.class)
                .exchangeToMono(response -> {
                    return getKafkaRestProxyResponseMono(topic, response);
                })
                .handle(KafkaHttpProxyProducerV2Impl::extracted);
    }

    private static void extracted(
            KafkaRestProxyResponse response,
            SynchronousSink<KafkaRestProxyResponse> sink
    ) {
        if (response.getOffsets() == null) {
            sink.error(new IllegalStateException("There is no offsets in the response"));
        }
        var offsets = response.getOffsets();
        for (var offset : offsets) {
            if (offset.getOffset() == null) {
                sink.error(new IllegalStateException(
                        "There is no offsets in the response, error=" + offset.getError()));
            }
        }
        sink.next(response);
    }

    @NotNull
    private static Mono<KafkaRestProxyResponse> getKafkaRestProxyResponseMono(
            String topic,
            ClientResponse response
    ) {
        if (response.statusCode() != HttpStatus.OK) {
            return Mono.error(
                    new IllegalStateException("Not OK response code='" + response.statusCode()
                                                      + "' when sending to '" + topic + "'"));
        }
        return response.bodyToMono(KafkaRestProxyResponse.class);
    }

}
