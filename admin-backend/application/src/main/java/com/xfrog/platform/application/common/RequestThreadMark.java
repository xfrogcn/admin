package com.xfrog.platform.application.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public void setMockTenantId(String mockTenantId) {
        this.mockTenantId = mockTenantId;
        log.info("set mock tenant id: {} {}", Thread.currentThread().getName(), mockTenantId);
    }

    public void setIgnoreTenant(boolean ignoreTenant) {
        isIgnoreTenant = ignoreTenant;
        log.info("set ignore tenant: {} {}", Thread.currentThread().getName(), ignoreTenant);
    }

    public void setIgnoreDataScope(boolean ignoreDataScope) {
        isIgnoreDataScope = ignoreDataScope;
        log.info("set ignore data scope: {} {}", Thread.currentThread().getName(), ignoreDataScope);
    }
}
