package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.permission.api.constant.PermissionOperationLogConstants;
import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateRoleRequestDTO;
import com.xfrog.platform.application.permission.service.RoleService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController implements RoleApi {
    private final RoleService roleService;

    @Authorization("admin:system:role:create")
    @Override
    @OperationLog(bizId = "#return", bizCode = "#p0.name", bizType = PermissionOperationLogConstants.BIZ_TYPE_ROLE, bizAction = OperationActionConstants.CREATE)
    public Long createRole(CreateRoleRequestDTO createRoleRequestDTO) {
        return roleService.createRole(createRoleRequestDTO);
    }

    @Authorization(value = "admin:system:role", demoDisabled = false)
    @Override
    public List<RoleDTO> listRoles() {
        return roleService.listRoles();
    }

    @Authorization(value = "admin:system:role", demoDisabled = false)
    @Override
    public List<PermissionItemDTO> getRolePermissionItems(Long roleId) {
        return roleService.getRolePermissionItems(roleId);
    }

    @Authorization("admin:system:role:edit")
    @Override
    @OperationLog(bizId = "#p0", bizCode = "#p1.name", bizType = PermissionOperationLogConstants.BIZ_TYPE_ROLE, bizAction = OperationActionConstants.UPDATE)
    public void updateRole(Long roleId, UpdateRoleRequestDTO updateRoleRequestDTO) {
        roleService.updateRole(roleId, updateRoleRequestDTO);
    }

    @Authorization("admin:system:role:delete")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_ROLE, bizAction = OperationActionConstants.DELETE)
    public void deleteRole(Long roleId) {
        roleService.deleteRole(roleId);
    }

    @Authorization("admin:system:role:disable")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_ROLE,
            bizActionSpel = "#p1 ? '" + OperationActionConstants.ENABLE + "': '" +OperationActionConstants.DISABLE + "'")
    public void enableRole(Long roleId, Boolean enabled) {
        roleService.enableRole(roleId, enabled);
    }

    @Authorization("admin:system:role:grant")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_ROLE, bizAction = OperationActionConstants.PERMISSION_CHANGE)
    public void grantPermissionItems(Long roleId, List<Long> permissionItemIds) {
        roleService.grantPermissionItems(roleId, permissionItemIds);
    }
}
