package com.xfrog.platform.infrastructure.persistent;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.xfrog.framework.common.SnowflakeUidGenerator;
import com.xfrog.platform.application.common.AnnotationScanner;
import com.xfrog.platform.infrastructure.persistent.config.EmbeddedMariaDbConfig;
import com.xfrog.platform.infrastructure.persistent.config.MybatisPlusConfig;
import com.xfrog.platform.infrastructure.persistent.config.TestEnvironmentExtension;
import com.xfrog.platform.infrastructure.persistent.handler.AutoFillMetaObjectHandler;
import com.xfrog.platform.infrastructure.persistent.handler.DataPermissionHandler;
import com.xfrog.platform.infrastructure.persistent.handler.TenantHandler;
import com.xfrog.platform.infrastructure.persistent.handler.datascope.OrganizationDataScopeHandler;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

//@DataJdbcTest
 @MybatisPlusTest
// Mybatis Plus自动配置
//@AutoConfigureMybatisPlus
@ImportAutoConfiguration(classes = {
        EmbeddedMariaDbConfig.class,
        MybatisPlusConfig.class,
        AutoFillMetaObjectHandler.class,
        SnowflakeUidGenerator.class,
        AnnotationScanner.class,
        TenantHandler.class,
        OrganizationDataScopeHandler.class,
        DataPermissionHandler.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("unit-test")
@ExtendWith(TestEnvironmentExtension.class)
public class BaseRepositoryTest {
}
