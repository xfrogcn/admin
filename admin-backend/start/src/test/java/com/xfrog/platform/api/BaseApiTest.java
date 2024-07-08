package com.xfrog.platform.api;

import com.xfrog.framework.common.JsonHelper;
import com.xfrog.platform.config.TestEnvironmentExtension;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("unit-test")
@ExtendWith(TestEnvironmentExtension.class)
@WebAppConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
@TestPropertySource(properties = {
        "admin.api-server.validOperatePermission=false",
        "admin.api-server.validExpiredToken=false"
})
public class BaseApiTest {
    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @SneakyThrows
    protected MockHttpServletRequestBuilder post(String url, Object body) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON);

        return processBody(builder, body);
    }

    @SneakyThrows
    protected MockHttpServletRequestBuilder put(String url, Object body) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON);

        return processBody(builder, body);
    }

    @SneakyThrows
    protected MockHttpServletRequestBuilder delete(String url, Object body) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON);

        return processBody(builder, body);
    }

    @SneakyThrows
    protected MockHttpServletRequestBuilder get(String url) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        return builder;
    }

    @SneakyThrows
    protected ResultActions request(RequestBuilder requestBuilder) {
        return mockMvc.perform(requestBuilder);
    }

    private MockHttpServletRequestBuilder processBody(MockHttpServletRequestBuilder builder, Object body) {
        if (body == null) {
            return builder;
        }
        String json = null;
        if (body instanceof String) {
            json = (String) body;
        } else {
            json = JsonHelper.serialize(body);
        }
        return builder.content(json);
    }
}
