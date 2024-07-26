package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.permission.api.constant.PermissionOperationLogConstants;
import com.xfrog.platform.application.permission.api.dto.CreateTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateTenantRequestDTO;
import com.xfrog.platform.application.permission.service.TenantService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TenantController implements TenantApi {
    private final TenantService tenantService;

    @Authorization("admin:platform:tenant:create")
    @Override
    @OperationLog(bizId = "#result", bizCode = "#p0.code", bizType = PermissionOperationLogConstants.BIZ_TYPE_TENANT, bizAction = OperationActionConstants.CREATE)
    public Long createTenant(CreateTenantRequestDTO tenantDTO) {
        return tenantService.createTenant(tenantDTO);
    }

    @Authorization(value = "admin:platform:tenant", demoDisabled = false)
    @Override
    public PageDTO<TenantDTO> listTenants(QueryTenantRequestDTO requestDTO) {
        return tenantService.listTenants(requestDTO);
    }

    @Authorization("admin:platform:tenant:edit")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_TENANT, bizAction = OperationActionConstants.UPDATE)
    public void updateTenant(Long tenantId, UpdateTenantRequestDTO tenantDTO) {
        tenantService.updateTenant(tenantId, tenantDTO);
    }

    @Authorization("admin:platform:tenant:disable")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_TENANT,
            bizActionSpel = "#p1 ? '" + OperationActionConstants.ENABLE + "': '" +OperationActionConstants.DISABLE + "'")
    public void enableTenant(Long tenantId, Boolean enabled) {
        tenantService.enableTenant(tenantId, enabled);
    }
}
