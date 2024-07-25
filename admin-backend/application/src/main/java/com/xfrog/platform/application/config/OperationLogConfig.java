package com.xfrog.platform.application.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.xfrog.framework.oplog.EnableOperationLog;
import com.xfrog.platform.application.base.event.OperationLogEventListener;
import com.xfrog.platform.application.base.service.OpLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name ="operation-log.enabled", havingValue = "true", matchIfMissing = true)
@EnableOperationLog
public class OperationLogConfig {
    @Value("${operation-log.thread-count: 4}")
    private Integer operationAsyncThreadCount;

    @Value("${operation-log.queue-size: 1000}")
    private Integer operationMaxQueueSize;

    @Bean("operationLogExecutorService")
    public ExecutorService operationLogExecutorService() {
        return  new ThreadPoolExecutor(
                operationAsyncThreadCount, // 核心线程数
                operationAsyncThreadCount, // 最大线程数，与核心线程数相同，表示线程池大小固定
                0L, // 空闲线程存活时间，因为最大线程数和核心线程数相同，这里设置为0
                TimeUnit.MILLISECONDS, // 空闲线程存活时间单位
                new LinkedBlockingQueue<>(operationMaxQueueSize), // 工作队列，最大容量为1000
                new ThreadFactoryBuilder().setNamePrefix("operation-log-").build() // 设置线程前缀为operation-log
        );
    }

    @Bean
    public OperationLogEventListener operationLogEventListener(OpLogService opLogService) {
        return new OperationLogEventListener(opLogService);
    }
}
