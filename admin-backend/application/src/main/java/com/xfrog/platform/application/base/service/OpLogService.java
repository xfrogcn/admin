package com.xfrog.platform.application.base.service;

import com.xfrog.framework.oplog.domain.OpLog;

import java.util.List;

public interface OpLogService {
    void saveOpLogs(List<OpLog> logs);
}