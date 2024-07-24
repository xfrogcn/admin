package com.xfrog.framework.principal;


import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
public final class PrincipalInfo {

    private static final PrincipalInfo SYSTEM_USER = new PrincipalInfo(0L, "system", 0L, "", true, "GLOBAL");

    private final Long userId;
    private final Long organizationId;

    private final String userName;
    private final String clientId;
    private final String tenantId;

    private final boolean isSystem;
    @Setter
    private List<DataPermissionItem> dataPermission;

    private PrincipalInfo(Long userId, String userName, Long organizationId, String clientId, String tenantId) {
        this(userId, userName, organizationId, clientId, false, tenantId);
    }

    private PrincipalInfo(Long userId, String userName, Long organizationId, String clientId, boolean isSystem, String tenantId) {
        this.userId = userId;
        this.userName = userName;
        this.organizationId = organizationId;
        this.clientId = clientId;
        this.isSystem = isSystem;
        this.tenantId = tenantId;
        this.dataPermission = new LinkedList<>();
    }

    public static PrincipalInfo create(Long userId, String userName, Long organizationId, String clientId, String tenantId) {
        return new PrincipalInfo(userId, userName, organizationId, clientId, tenantId);
    }

    public static PrincipalInfo system() {
        return SYSTEM_USER;
    }
}
