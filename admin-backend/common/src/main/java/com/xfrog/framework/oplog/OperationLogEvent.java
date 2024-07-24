package com.xfrog.framework.oplog;

import com.xfrog.framework.oplog.domain.OpLog;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class OperationLogEvent extends ApplicationEvent {
    OperationLogEvent(List<OpLog> logs) {
        super(logs);
    }

    public static OperationLogEvent of(List<OpLog> logs) {
        return new OperationLogEvent(logs);
    }

    @SuppressWarnings("unchecked")
    public List<OpLog> getLogs() {
        return (List<OpLog>) getSource();
    }
}
