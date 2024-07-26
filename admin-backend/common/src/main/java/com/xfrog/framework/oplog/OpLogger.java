package com.xfrog.framework.oplog;

import com.xfrog.framework.oplog.domain.OpLog;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class OpLogger {

    private final OperationLogPublisher publisher;

    /**
     * 操作成功日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     */
    public void success(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, true, null, null, null);
    }

    /**
     * 操作成功日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     * @param executionTime 执行时长
     */
    public void success(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, true, executionTime, null, null);
    }

    /**
     * 操作成功日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     * @param executionTime 执行时长
     * @param message 日志消息
     */
    public void success(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, true, executionTime, message, null);
    }

    /**
     * 操作成功日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     * @param executionTime 执行时长
     * @param message 日志消息
     * @param extra 扩展消息
     */
    public void success(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message, String extra) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, true, executionTime, message, extra);
    }

    /**
     * 操作失败日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     */
    public void fail(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, false, null, null, null);
    }

    /**
     * 操作失败日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     * @param executionTime 执行时长
     */
    public void fail(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, false, executionTime, null, null);
    }

    /**
     * 操作失败日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     * @param executionTime 执行时长
     * @param message 日志消息
     */
    public void fail(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, false, executionTime, message, null);
    }

    /**
     * 操作失败日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     * @param executionTime 执行时长
     * @param message 日志消息
     * @param extra 扩展消息
     */
    public void fail(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode, Long executionTime, String message, String extra) {
        log(operatorId, logType, bizType, bizAction, bizId, bizCode, false, executionTime, message, extra);
    }

    /**
     * 操作日志
     * @param operatorId 操作人ID
     * @param logType 日志类型
     * @param bizType 业务类型
     * @param bizAction 业务动作
     * @param bizId 业务ID
     * @param bizCode 业务编码
     * @param success 是否成功
     * @param executionTime 执行时长
     * @param message 日志消息
     * @param extra 扩展消息
     */
    public void log(Long operatorId, String logType, String bizType, String bizAction, String bizId, String bizCode, boolean success, Long executionTime, String message, String extra) {
        OpLog.OpLogBuilder builder = OpLog.builder()
                .tag(logType)
                .bizType(bizType)
                .bizAction(bizAction)
                .bizId(bizId)
                .bizCode(bizCode)
                .message(message)
                .extra(extra)
                .success(success)
                .executeTime(executionTime)
                .operatorId(operatorId);

        String traceId = MDC.get("traceId");
        if (StringUtils.hasText(traceId)) {
            builder.requestId(traceId);
        }

        publisher.publish(List.of(builder.build()));
    }


}
