package com.xfrog.platform.infrastructure.persistent.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.xfrog.platform.infrastructure.persistent.handler.DataPermissionHandler;
import com.xfrog.platform.infrastructure.persistent.handler.TenantHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantHandler tenantHandler, DataPermissionHandler dataPermissionHandler) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(tenantHandler);
        mybatisPlusInterceptor.addInnerInterceptor(tenantInterceptor);

        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor(dataPermissionHandler);
        mybatisPlusInterceptor.addInnerInterceptor(dataPermissionInterceptor);

        return mybatisPlusInterceptor;
    }
}
