package com.xfrog.platform.infrastructure.persistent;

import com.xfrog.platform.infrastructure.persistent.config.EmbeddedMariaDbConfig;
import com.xfrog.platform.infrastructure.persistent.config.TestEnvironmentExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@DataJdbcTest
@ImportAutoConfiguration(classes = {EmbeddedMariaDbConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(TestEnvironmentExtension.class)
@ActiveProfiles("unit-test")
public class BaseJDBCRepositoryTest {
}
