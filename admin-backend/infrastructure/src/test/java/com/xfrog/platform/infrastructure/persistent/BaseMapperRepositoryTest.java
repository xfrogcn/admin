package com.xfrog.platform.infrastructure.persistent;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.xfrog.framework.common.SnowflakeUidGenerator;
import com.xfrog.platform.infrastructure.persistent.handler.AutoFillMetaObjectHandler;
import com.xfrog.platform.infrastructure.persistent.config.EmbeddedMariaDbConfig;
import com.xfrog.platform.infrastructure.persistent.config.TestEnvironmentExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@MybatisPlusTest
@ImportAutoConfiguration(classes = {EmbeddedMariaDbConfig.class, SnowflakeUidGenerator.class, AutoFillMetaObjectHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(TestEnvironmentExtension.class)
@ActiveProfiles("unit-test")
public class BaseMapperRepositoryTest {
}
