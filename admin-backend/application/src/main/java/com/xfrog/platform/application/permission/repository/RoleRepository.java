package com.xfrog.platform.application.permission.repository;


import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;

import java.util.List;

public interface RoleRepository {
    List<RoleDTO> queryAll();

    List<RoleDTO> queryByIds(List<Long> roleIds);

    List<PermissionItemDTO> queryRolePermissions(Long roleId);
}
