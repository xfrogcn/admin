package com.xfrog.framework.oplog;

import com.xfrog.framework.principal.CurrentPrincipalContext;

public class DefaultOperatorIdProvider implements OperatorIdProvider {
    @Override
    public Long getOperatorId() {
        return CurrentPrincipalContext.currentPrincipalOrSystem().getUserId();
    }
}
