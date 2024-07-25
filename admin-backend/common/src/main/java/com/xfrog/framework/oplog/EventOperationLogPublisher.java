package com.xfrog.framework.oplog;

import com.xfrog.framework.oplog.domain.OpLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
public class EventOperationLogPublisher implements OperationLogPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(List<OpLog> opLogs) {
        if (CollectionUtils.isEmpty(opLogs)) {
            return;
        }

        eventPublisher.publishEvent(OperationLogEvent.of(opLogs));
    }
}
