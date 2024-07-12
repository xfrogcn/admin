package com.xfrog.platform.api.permission.fixtures;

import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.domain.permission.repository.PermissionItemDomainRepository;
import com.xfrog.platform.domain.permission.repository.RolePermissionItemDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionApiFixtures {
    public final static String SQL_TRUNCATE_PERMISSION_ITEM = "truncate table permission_items";
    public final static String SQL_TRUNCATE_ROLE_PERMISSION_ITEM =  "truncate table role_permission_items";
    public final static String SQL_TRUNCATE_ROLES =  "truncate table roles";
    public final static String SQL_TRUNCATE_ORGANIZATION = "truncate table organizations";
    public final static String SQL_TRUNCATE_TENANTS =  "truncate table tenants";
    public final static String SQL_TRUNCATE_USERS =  "truncate table users";
    public final static String SQL_TRUNCATE_USER_ROLES =  "truncate table user_roles";

    @Autowired
    public PermissionItemDomainRepository permissionItemDomainRepository;
    @Autowired
    public RolePermissionItemDomainRepository rolePermissionItemDomainRepository;

    public PermissionItem createAndSavePermissionItem(String code, Long parentId) {
        PermissionItem permissionItem = PermissionFixtures.createDefaultPermissionItem()
                .code(code)
                .parentId(parentId)
                .platform(false)
                .build();
        permissionItem.setId(null);
        permissionItem =  permissionItemDomainRepository.save(permissionItem);
        return permissionItem;
    }

    public PermissionItem createAndSavePlatformPermissionItem(String code, Long parentId) {
        PermissionItem permissionItem = PermissionFixtures.createDefaultPermissionItem()
                .code(code)
                .parentId(parentId)
                .platform(true)
                .build();
        permissionItem.setId(null);
        permissionItem =  permissionItemDomainRepository.save(permissionItem);
        return permissionItem;
    }

    public RolePermissionItem createAndSaveRolePermissionItem(Long roleId, Long permissionItemId) {
        RolePermissionItem rolePermissionItem = RolePermissionItem.builder()
                .roleId(roleId)
                .permissionItemId(permissionItemId)
                .build();
        rolePermissionItem.setId(null);
        rolePermissionItem = rolePermissionItemDomainRepository.save(rolePermissionItem);
        return rolePermissionItem;
    }
}
