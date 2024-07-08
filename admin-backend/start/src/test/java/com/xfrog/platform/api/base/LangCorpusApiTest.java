package com.xfrog.platform.api.base;

import com.xfrog.platform.api.BaseApiTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LangCorpusApiTest extends BaseApiTest {

    @SneakyThrows
    @Test
    void should_success() {
        request(get("/api/langcorpus/1"))
                .andExpect(status().isOk());

    }
}
