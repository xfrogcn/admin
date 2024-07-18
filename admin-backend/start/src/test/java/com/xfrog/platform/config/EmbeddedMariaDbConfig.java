package com.xfrog.platform.config;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Slf4j
@Configuration
@ConditionalOnProperty(name = "mariaDB4j.enabled", havingValue = "true")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class EmbeddedMariaDbConfig {

    @Value("${mariaDB4j.db-name}")
    private String dbName;


    @Bean
    public DataSource dataSource() throws ManagedProcessException {

        MariaDB4jSpringService mariaDB4jSpringService = TestEnvironmentExtension.getMariaDB4jService();
        // 设置字符编码
        mariaDB4jSpringService.getDB().run(String.format("create database if not exists `%s` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';", dbName));

        String url = mariaDB4jSpringService.getConfiguration().getURL(dbName) + "?useUnicode=true&characterEncoding=utf-8";
        log.info("test mariaDb url: {}", url);
        DataSource dataSource = DataSourceBuilder.create()
                .url(url)
                .build();

        return dataSource;
    }


}
