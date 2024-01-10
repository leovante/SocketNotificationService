package com.nlmk.adp.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация маппера.
 */
@Configuration
public class ObjectMapperConfig {

    /**
     * Конфиг jackson (для invalid notif логирования).
     *
     * @return конфиг jackson.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.mixIn(
                    org.apache.avro.specific.SpecificRecord.class,
                    JacksonIgnoreAvroProperties.class
            );
        };
    }

    abstract class JacksonIgnoreAvroProperties {

        @JsonIgnore
        public abstract org.apache.avro.Schema getClassSchema();

        @JsonIgnore
        public abstract org.apache.avro.specific.SpecificData getSpecificData();

        @JsonIgnore
        public abstract java.lang.Object get(int field);

        @JsonIgnore
        public abstract org.apache.avro.Schema getSchema();

    }

}
