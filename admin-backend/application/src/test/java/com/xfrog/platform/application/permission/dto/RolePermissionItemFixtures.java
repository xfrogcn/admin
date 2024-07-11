package com.xfrog.platform.application.permission.dto;

import com.xfrog.framework.common.SnowflakeUidGenerator;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;

public class RolePermissionItemFixtures {
    public static RolePermissionItem.RolePermissionItemBuilder createDefaultRolePermissionItem() {
        return RolePermissionItem.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .permissionItemId(1L)
                .roleId(1L);
    }
}
