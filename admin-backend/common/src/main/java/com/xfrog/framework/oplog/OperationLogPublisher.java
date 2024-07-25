package com.xfrog.framework.oplog;

import com.xfrog.framework.oplog.domain.OpLog;

import java.util.List;

public interface OperationLogPublisher {
    void publish(List<OpLog> opLogs);
}
