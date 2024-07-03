package com.xfrog.platform.application.config;

import com.xfrog.framework.common.EventPublisher;
import com.xfrog.framework.common.SnowflakeUidGenerator;
import com.xfrog.framework.common.UidGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminConfig {
    @Bean
    public UidGenerator uidGenerator() {
        return new SnowflakeUidGenerator();
    }

    @Bean
    public EventPublisher eventPublisher() {
        return new EventPublisher();
    }
}
