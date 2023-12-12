package com.nlmk.adp.config;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Стандартный файл конфигурации.
 */
@EnableAsync
@Configuration
@EnableTransactionManagement
public class ApplicationConfig {

    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    void setup() {
        objectMapper.addMixIn(org.apache.avro.specific.SpecificRecord.class, JacksonIgnoreAvroProperties.class);
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
