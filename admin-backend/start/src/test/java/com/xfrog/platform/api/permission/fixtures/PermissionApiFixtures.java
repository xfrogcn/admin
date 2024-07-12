package com.xfrog.platform.api.permission.fixtures;

import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.aggregate.PermissionFixtures;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import com.xfrog.platform.domain.permission.aggregate.Role;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.domain.permission.repository.OrganizationDomainRepository;
import com.xfrog.platform.domain.permission.repository.PermissionItemDomainRepository;
import com.xfrog.platform.domain.permission.repository.RoleDomainRepository;
import com.xfrog.platform.domain.permission.repository.RolePermissionItemDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PermissionApiFixtures {
    public final static String SQL_TRUNCATE_PERMISSION_ITEM = "truncate table permission_items";
    public final static String SQL_TRUNCATE_ROLE_PERMISSION_ITEM =  "truncate table role_permission_items";
    public final static String SQL_TRUNCATE_ROLES =  "delete from roles WHERE id != 1803299774525980674";
    // 不删除组织为1的数据，用于数据权限控制
    public final static String SQL_TRUNCATE_ORGANIZATION = "delete from organizations WHERE id != 1";
    public final static String SQL_TRUNCATE_TENANTS =  "delete from tenants where id != 1";
    public final static String SQL_TRUNCATE_USERS =  "delete from users WHERE id != 1";
    public final static String SQL_TRUNCATE_USER_ROLES =  "delete from user_roles WHERE role_id != 1803299774525980674";
    public final static String SQL_TRUNCATE_DATA_SCOPES =  "delete from data_scopes WHERE target_id != 1803299774525980674";

    @Autowired
    public PermissionItemDomainRepository permissionItemDomainRepository;
    @Autowired
    public RolePermissionItemDomainRepository rolePermissionItemDomainRepository;
    @Autowired
    public OrganizationDomainRepository organizationDomainRepository;
    @Autowired
    public RoleDomainRepository roleDomainRepository;

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

    public Organization saveOrganization(Organization organization) {
        organization.setId(null);
        return organizationDomainRepository.save(organization);
    }

    public Role saveRole(Role role) {
        role.setId(null);
        return roleDomainRepository.save(role);
    }
}
