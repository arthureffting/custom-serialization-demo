package com.example.globalserialization.demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SerializationDemoTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    void testCarSerialization() throws Exception {
        this.mockMvc.perform(get("/car"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturer").value("Koyota"))
                .andExpect(jsonPath("$.model").value("torolla-identifier"))
                .andExpect(jsonPath("$.position.latitude").value(0.0))
                .andExpect(jsonPath("$.position.longitude").value(0.0))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    System.out.println(mvcResult.getResponse().getContentAsString());
                });
    }


}
