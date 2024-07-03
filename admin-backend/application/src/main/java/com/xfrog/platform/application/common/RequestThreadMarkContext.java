package com.xfrog.platform.application.common;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.xfrog.framework.principal.CurrentPrincipalContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestThreadMarkContext {
    private static final TransmittableThreadLocal<RequestThreadMark> REQUEST_THREAD_MARK_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static RequestThreadMark threadMark() {
        RequestThreadMark requestThreadMark = REQUEST_THREAD_MARK_THREAD_LOCAL.get();
        if (requestThreadMark == null) {
            requestThreadMark = new RequestThreadMark();
            REQUEST_THREAD_MARK_THREAD_LOCAL.set(requestThreadMark);
        }
        return requestThreadMark;
    }


    public static void clearThreadMark() {
        REQUEST_THREAD_MARK_THREAD_LOCAL.remove();
    }

    public static String currentTenantId() {
        String tenantId = RequestThreadMarkContext.threadMark().getMockTenantId();
        if (tenantId == null) {
            tenantId = CurrentPrincipalContext.currentPrincipalOrSystem().getTenantId();
        }
        return tenantId;
    }
}
