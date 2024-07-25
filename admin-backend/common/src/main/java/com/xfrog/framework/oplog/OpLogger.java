package com.xfrog.framework.oplog;

import com.xfrog.framework.oplog.domain.OpLog;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OpLogger {

    private final OperationLogPublisher publisher;

    public void success(String logType, String bizType, String bizAction, String bizId, String bizCode) {
        log(logType, bizType, bizAction, bizId, bizCode, true, null, null, null);
    }

    public void success(String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime) {
        log(logType, bizType, bizAction, bizId, bizCode, true, executionTime, null, null);
    }

    public void success(String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message) {
        log(logType, bizType, bizAction, bizId, bizCode, true, executionTime, message, null);
    }

    public void success(String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message, String extra) {
        log(logType, bizType, bizAction, bizId, bizCode, true, executionTime, message, extra);
    }

    public void fail(String logType, String bizType, String bizAction, String bizId, String bizCode) {
        log(logType, bizType, bizAction, bizId, bizCode, false, null, null, null);
    }

    public void fail(String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime) {
        log(logType, bizType, bizAction, bizId, bizCode, false, executionTime, null, null);
    }

    public void fail(String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message) {
        log(logType, bizType, bizAction, bizId, bizCode, false, executionTime, message, null);
    }

    public void fail(String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message, String extra) {
        log(logType, bizType, bizAction, bizId, bizCode, false, executionTime, message, extra);
    }

    public void log(String logType, String bizType, String bizAction, String bizId, String bizCode, boolean success, Long executionTime, String message, String extra) {
        OpLog opLog = OpLog.builder()
                .tag(logType)
                .bizType(bizType)
                .bizAction(bizAction)
                .bizId(bizId)
                .bizCode(bizCode)
                .message(message)
                .extra(extra)
                .success(success)
                .executeTime(executionTime)
                .build();
        publisher.publish(List.of(opLog));
    }


}
