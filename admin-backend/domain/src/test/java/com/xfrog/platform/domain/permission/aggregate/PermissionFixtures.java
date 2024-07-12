package com.xfrog.platform.domain.permission.aggregate;

import com.xfrog.framework.common.SnowflakeUidGenerator;

public class PermissionFixtures {
    public static PermissionItem.PermissionItemBuilder createDefaultPermissionItem() {
        return PermissionItem.builder()
                .code("code")
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .name("name")
                .parentId(1L)
                .platform(true)
                .type("type");
    }

    public static RolePermissionItem.RolePermissionItemBuilder createDefaultRolePermissionItem() {
        return RolePermissionItem.builder()
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .permissionItemId(1L)
                .roleId(1L);
    }
}