package com.nlmk.adp.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Утилита для работы с objectMapper.
 */
@Slf4j
@Configuration
public class ObjectMapperHelper {

    /**
     * objectMapperHelperNested.
     *
     * @return ObjectMapperHelperNested
     */
    @SuppressWarnings("static-access")
    @Bean
    protected ObjectMapperHelperNested objectMapperHelperNested() {
        ObjectMapperHelperNested beanFactory = new ObjectMapperHelperNested();
        return beanFactory.getInstance();
    }

    /**
     * getObjectMapper.
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return ObjectMapperHelperNested.getInstance().getObjectMapper();
    }

    /**
     * writeValueAsString.
     *
     * @param clazz
     *          clazz
     * @param obj
     *          obj
     * @return String
     */
    public static String writeValueAsString(Class<?> clazz, Object obj) {
        if (getObjectMapper() == null) {
            return "";
        }
        try {
            return getObjectMapper()
                    .writerFor(clazz)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
        }
        return "";
    }

    /**
     * writeValueAsString.
     *
     * @param obj
     *          obj
     * @return String
     */
    public static String writeValueAsString(Object obj) {
        if (getObjectMapper() == null) {
            return "";
        }
        try {
            return getObjectMapper()
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
        }
        return "";
    }

    @Getter
    private static final class ObjectMapperHelperNested {

        public static final ObjectMapperHelperNested INSTANCE = new ObjectMapperHelperNested();

        @Autowired
        protected ObjectMapper objectMapper;

        private ObjectMapperHelperNested() {
        }

        public static ObjectMapperHelperNested getInstance() {
            return INSTANCE;
        }

    }

}
