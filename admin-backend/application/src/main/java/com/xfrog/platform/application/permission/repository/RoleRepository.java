package com.xfrog.platform.application.permission.repository;


import com.xfrog.framework.repository.CacheableApplicationRepository;
import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.dto.RoleDTO;

import java.util.List;

public interface RoleRepository extends CacheableApplicationRepository<RoleDTO> {
    List<RoleDTO> queryAll();

    List<PermissionItemDTO> queryRolePermissions(Long roleId);
}
