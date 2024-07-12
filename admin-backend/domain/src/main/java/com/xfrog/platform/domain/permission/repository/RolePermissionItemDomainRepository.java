package com.xfrog.platform.domain.permission.repository;

import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.framework.repository.DomainRepository;

import java.util.List;

public interface RolePermissionItemDomainRepository extends DomainRepository<RolePermissionItem> {
    List<RolePermissionItem> getByRoleId(Long roleId);

    List<RolePermissionItem> getByPermissionItemId(Long permissionItemId);
}
