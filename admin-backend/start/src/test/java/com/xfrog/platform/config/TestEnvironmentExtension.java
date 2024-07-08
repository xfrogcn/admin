package com.xfrog.platform.config;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

/**
 * 用于准备测试环境的测试扩展
 * 初始化测试数据库
 * 初始化内嵌Redis
 */
@Slf4j
public class TestEnvironmentExtension implements BeforeAllCallback {

    public static class MariaDB4jSpringServiceEx extends MariaDB4jSpringService {
        @Override
        public void start() {
            killProcess();
            for (int i = 0; i< 3; i++) {
                try {
                    super.start();
                    break;
                } catch (Exception ex) {
                    // 自动关闭遗留mariadb进程。比如，在调试测试过程中，终止测试进程，导致mariadb为关闭
                    killProcess();
                    super.stop();
                }
            }
        }

        private void killProcess() {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash",
                    "-c",
                    String.format("lsof -t -i:%s | xargs kill -9", this.getConfiguration().getPort()));
            try {
                Process process = processBuilder.start();
                int exitCode = process.waitFor();
                System.out.println("自动终止遗留mariaDB进程完成：" + exitCode);
                Thread.sleep(300);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Getter
    private static volatile MariaDB4jSpringServiceEx mariaDB4jService;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.setProperty("LIQUIBASE_FILE_ENCODING", "UTF-8");
        if (mariaDB4jService == null) {
            synchronized (TestEnvironmentExtension.class) {
                if (mariaDB4jService == null) {
                    mariaDB4jService = new MariaDB4jSpringServiceEx();
                    mariaDB4jService.start();
                    // 注意：在mariaDB已经注册了JVM ShutdownHook，在JVM退出前自动关闭数据库
                    log.info("启动测试数据库...");
                }
            }
        }
    }
}
