package com.xfrog.platform.domain.permission.aggregate;

import com.xfrog.framework.common.SnowflakeUidGenerator;
import com.xfrog.platform.domain.share.permission.OrganizationStatus;

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

    public static Organization.OrganizationBuilder createDefaultOrganization() {
        return Organization.builder()
                .code("00010001")
                .createdBy(1L)
                .createdTime(null)
                .deleted(false)
                .deletedBy(null)
                .deletedTime(null)
                .displayOrder(1)
                .status(OrganizationStatus.NORMAL)
                .id(SnowflakeUidGenerator.INSTANCE.nextId())
                .level(1)
                .name("name")
                .parentId(1L);
    }
}