package com.nlmk.adp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ApplicationInitTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void contextLoads() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(MockMvcResultMatchers.content().string("test"));
    }

}
