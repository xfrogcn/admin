package com.xfrog.platform.application.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RequestThreadMark {
    // Mock的租户ID -- 查询或插入时，先取此值，如果为空，则取当前登录用户所属租户ID
    private String mockTenantId;
    // 是否忽略租户 -- 查询时不自动加入租户条件
    private boolean isIgnoreTenant;
    // 是否忽略数据权限
    private boolean isIgnoreDataScope;

    public static class AutoRecovery implements AutoCloseable {
        private final Consumer<RequestThreadMark> recovery;
        private final RequestThreadMark mark;

        public AutoRecovery(Consumer<RequestThreadMark> recovery, RequestThreadMark mark) {
            this.recovery = recovery;
            this.mark = mark;
        }

        @Override
        public void close() throws Exception {
           if (this.mark != null && this.recovery != null) {
               this.recovery.accept(this.mark);
           }
        }
    }

    public AutoRecovery setMockTenantId(String mockTenantId) {
        String originalMockTenantId = this.mockTenantId;
        this.mockTenantId = mockTenantId;
        log.info("set mock tenant id: {} {}", Thread.currentThread().getName(), mockTenantId);
        return new AutoRecovery((mark) -> mark.mockTenantId = originalMockTenantId, this);
    }

    public AutoRecovery setIgnoreTenant(boolean ignoreTenant) {
        boolean originalIgnoreTenant = isIgnoreTenant;
        isIgnoreTenant = ignoreTenant;
        log.info("set ignore tenant: {} {}", Thread.currentThread().getName(), ignoreTenant);
        return new AutoRecovery((mark) -> mark.isIgnoreTenant = originalIgnoreTenant, this);
    }

    public AutoRecovery setIgnoreDataScope(boolean ignoreDataScope) {
        boolean originalIgnoreDataScope = isIgnoreDataScope;
        isIgnoreDataScope = ignoreDataScope;
        log.info("set ignore data scope: {} {}", Thread.currentThread().getName(), ignoreDataScope);
        return new AutoRecovery((mark) -> mark.isIgnoreDataScope = originalIgnoreDataScope, this);
    }
}
