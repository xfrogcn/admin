package com.xfrog.framework.principal;


import com.alibaba.ttl.TransmittableThreadLocal;

public class CurrentPrincipalContext {
    private static final TransmittableThreadLocal<PrincipalInfo> PRINCIPAL_INFO_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void setCurrentPrincipal(PrincipalInfo principalInfo) {
        PRINCIPAL_INFO_THREAD_LOCAL.set(principalInfo);
    }

    public boolean hasCurrentPrincipal() {
        return PRINCIPAL_INFO_THREAD_LOCAL.get() != null;
    }

    public static PrincipalInfo currentPrincipal() {
        return PRINCIPAL_INFO_THREAD_LOCAL.get();
    }

    public static PrincipalInfo currentPrincipalOrSystem() {
        return PRINCIPAL_INFO_THREAD_LOCAL.get() == null ? PrincipalInfo.system() : PRINCIPAL_INFO_THREAD_LOCAL.get();
    }


    public static void clearCurrentPrincipal() {
        PRINCIPAL_INFO_THREAD_LOCAL.remove();
    }
}
