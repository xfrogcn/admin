package com.xfrog.platform.application.base.event;

import com.xfrog.framework.oplog.OperationLogEvent;
import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.PrincipalInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public class OperationLogEventListener implements ApplicationListener<OperationLogEvent> {
    @Override
    @Async("operationLogExecutorService")
    public void onApplicationEvent(OperationLogEvent event) {
        PrincipalInfo principalInfo = CurrentPrincipalContext.currentPrincipal();
        log.info("logs");
    }
}
