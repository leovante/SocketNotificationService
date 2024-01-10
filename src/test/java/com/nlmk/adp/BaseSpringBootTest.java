package com.nlmk.adp;

import java.io.InputStreamReader;
import java.io.Reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nlmk.adp.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public class BaseSpringBootTest extends AbstractTestcontainers {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    public <T> T getResource(String resourceName, Class<T> clazz) {
        T res = null;

        try (Reader json = new InputStreamReader(BaseSpringBootTest.class.getResourceAsStream(resourceName))) {
            res = objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("unable to get resource");
            throw (RuntimeException) ExceptionUtils.rethrow(e);
        }
        return res;
    }

}
