package com.xfrog.framework.oplog;

import com.xfrog.framework.oplog.domain.OpLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OpLoggerTest {

    @Mock
    private OperationLogPublisher publisher;

    @InjectMocks
    private OpLogger opLogger;

    @Test
    void success_5p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";

        // Act
        opLogger.success(logType, bizType, bizAction, bizId, bizCode);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(true, opLog.getSuccess());
        assertNull(opLog.getMessage());
        assertNull(opLog.getExtra());
        assertNull(opLog.getExecuteTime());
    }

    @Test
    void success_6p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";
        Long execTime = 1L;

        // Act
        opLogger.success(logType, bizType, bizAction, bizId, bizCode, execTime);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(true, opLog.getSuccess());
        assertNull(opLog.getMessage());
        assertNull(opLog.getExtra());
        assertEquals(execTime, opLog.getExecuteTime());
    }

    @Test
    void success_7p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";
        Long execTime = 1L;
        String message = "message";

        // Act
        opLogger.success(logType, bizType, bizAction, bizId, bizCode, execTime, message);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(true, opLog.getSuccess());
        assertEquals(message, opLog.getMessage());
        assertNull(opLog.getExtra());
        assertEquals(execTime, opLog.getExecuteTime());
    }

    @Test
    void success_8p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";
        Long execTime = 1L;
        String message = "message";
        String extra = "extra";

        // Act
        opLogger.success(logType, bizType, bizAction, bizId, bizCode, execTime, message, extra);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(true, opLog.getSuccess());
        assertEquals(message, opLog.getMessage());
        assertEquals(extra, opLog.getExtra());
        assertEquals(execTime, opLog.getExecuteTime());
    }

    @Test
    void fail_5p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";

        // Act
        opLogger.fail(logType, bizType, bizAction, bizId, bizCode);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(false, opLog.getSuccess());
        assertNull(opLog.getMessage());
        assertNull(opLog.getExtra());
        assertNull(opLog.getExecuteTime());
    }

    @Test
    void fail_6p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";
        Long execTime = 1L;

        // Act
        opLogger.fail(logType, bizType, bizAction, bizId, bizCode, execTime);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(false, opLog.getSuccess());
        assertNull(opLog.getMessage());
        assertNull(opLog.getExtra());
        assertEquals(execTime, opLog.getExecuteTime());
    }

    @Test
    void fail_7p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";
        Long execTime = 1L;
        String message = "message";

        // Act
        opLogger.fail(logType, bizType, bizAction, bizId, bizCode, execTime, message);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(false, opLog.getSuccess());
        assertEquals(message, opLog.getMessage());
        assertNull(opLog.getExtra());
        assertEquals(execTime, opLog.getExecuteTime());
    }

    @Test
    void fail_8p() {
        // Arrange
        String logType = "testType";
        String bizType = "testBizType";
        String bizAction = "testBizAction";
        String bizId = "testBizId";
        String bizCode = "testBizCode";
        Long execTime = 1L;
        String message = "message";
        String extra = "extra";

        // Act
        opLogger.fail(logType, bizType, bizAction, bizId, bizCode, execTime, message, extra);

        // Assert
        ArgumentCaptor<List<OpLog>> captor = ArgumentCaptor.forClass(List.class);
        verify(publisher).publish(captor.capture());
        List<OpLog> opLogs = captor.getValue();

        assertEquals(1, opLogs.size());
        OpLog opLog = opLogs.get(0);
        assertEquals(logType, opLog.getTag());
        assertEquals(bizType, opLog.getBizType());
        assertEquals(bizAction, opLog.getBizAction());
        assertEquals(bizId, opLog.getBizId());
        assertEquals(bizCode, opLog.getBizCode());
        assertEquals(false, opLog.getSuccess());
        assertEquals(message, opLog.getMessage());
        assertEquals(extra, opLog.getExtra());
        assertEquals(execTime, opLog.getExecuteTime());
    }
}