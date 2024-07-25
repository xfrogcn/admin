package com.xfrog.platform.application.base.event;

import com.xfrog.framework.oplog.OperationLogEvent;
import com.xfrog.platform.application.base.service.OpLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public class OperationLogEventListener implements ApplicationListener<OperationLogEvent> {

    private final OpLogService opLogService;

    @Override
    @Async("operationLogExecutorService")
    public void onApplicationEvent(OperationLogEvent event) {
        opLogService.saveOpLogs(event.getLogs());
    }
}
