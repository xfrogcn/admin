package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.permission.api.constant.PermissionOperationLogConstants;
import com.xfrog.platform.application.permission.api.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.UpdatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.service.PermissionItemService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PermissionItemController implements PermissionItemApi {

    private final PermissionItemService permissionItemService;

    @Authorization("admin:platform:permissionitem:create")
    @Override
    @OperationLog(bizId = "#result", bizCode = "#p0.code", bizType = PermissionOperationLogConstants.BIZ_TYPE_PERMISSION_ITEM, bizAction = OperationActionConstants.CREATE)
    public Long createPermissionItem(CreatePermissionItemRequestDTO permissionItemRequestDTO) {
        return permissionItemService.createPermissionItem(permissionItemRequestDTO);
    }

    @Authorization("admin:platform:permissionitem:edit")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_PERMISSION_ITEM, bizAction = OperationActionConstants.UPDATE)
    public void updatePermissionItem(Long permissionItemId, UpdatePermissionItemRequestDTO permissionItem) {
        permissionItemService.updatePermissionItem(permissionItemId, permissionItem);
    }

    @Authorization(value = "admin:system:role", demoDisabled = false)
    @Override
    public List<PermissionItemDTO> listPermissionItems() {
        return permissionItemService.listPermissionItems(false);
    }

    @Override
    @Authorization(value = "admin:platform:permissionitem|admin:platform:grantplatformpermission", demoDisabled = false)
    public List<PermissionItemDTO> listPermissionItemsFormPlatform() {
        return permissionItemService.listPermissionItems(true);
    }

    @Authorization("admin:platform:permissionitem:delete")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_PERMISSION_ITEM, bizAction = OperationActionConstants.DELETE)
    public void deletePermissionItem(Long permissionItemId) {
        permissionItemService.deletePermissionItem(permissionItemId);
    }
}
