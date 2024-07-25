package com.xfrog.platform.domain.base.aggregate;

import com.xfrog.framework.oplog.domain.OpLog;

public class OpLogFixtures {
    public static OpLog.OpLogBuilder createDefaultOpLog() {
        return OpLog.builder()
                .bizId("bizId")
                .bizCode("bizCode")
                .bizType("bizType")
                .bizAction("bizAction")
                .requestId("requestId")
                .tag("tag")
                .message("message")
                .extra("extra")
                .success(true)
                .executeTime(100L);
    }
}
