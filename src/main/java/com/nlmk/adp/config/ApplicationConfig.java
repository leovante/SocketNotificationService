package com.nlmk.adp.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Стандартный файл конфигурации.
 */
@EnableAsync
@Configuration
public class ApplicationConfig {

    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .mixIn(org.apache.avro.specific.SpecificRecord.class, JacksonIgnoreAvroProperties.class);
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
