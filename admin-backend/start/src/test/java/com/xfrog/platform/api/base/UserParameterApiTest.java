package com.xfrog.platform.api.base;


import com.xfrog.platform.api.base.fixtures.BaseApiFixtures;
import com.xfrog.platform.application.base.dto.UpdateUserParameterRequestDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserParameterApiTest  extends BaseBaseApiTest {
    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_LANG, BaseApiFixtures.SQL_TRUNCATE_USER_PARAMTERS})
    void getUserSettings_should_success() {
        request(get(url("/api/user-parameters/{application}", "admin-ui")))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @Sql(statements = {BaseApiFixtures.SQL_TRUNCATE_USER_PARAMTERS})
    void updateUserParameters_should_success() {

        UpdateUserParameterRequestDTO requestDTO = UpdateUserParameterRequestDTO.builder()
                .parameters(Map.of("theme", "dark", "language", "zh-CN"))
                .build();

        request(put(url("/api/user-parameters/{application}", "admin-ui"), requestDTO))
                .andExpect(status().isOk());
    }
}
