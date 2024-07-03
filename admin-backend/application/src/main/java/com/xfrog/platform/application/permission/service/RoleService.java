package com.xfrog.platform.application.permission.service;


import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateRoleRequestDTO;

import java.util.List;

public interface RoleService {
    Long createRole(CreateRoleRequestDTO createRoleRequestDTO);

    Long createTenantRole(CreateRoleRequestDTO createRoleRequestDTO, String tenantId);

    List<RoleDTO> listRoles();

    List<PermissionItemDTO> getRolePermissionItems(Long roleId);

    void updateRole(Long roleId, UpdateRoleRequestDTO updateRoleRequestDTO);

    void deleteRole(Long roleId);

    void enableRole(Long roleId, Boolean enabled);

    void grantPermissionItems(Long roleId, List<Long> permissionItemIds);
}
