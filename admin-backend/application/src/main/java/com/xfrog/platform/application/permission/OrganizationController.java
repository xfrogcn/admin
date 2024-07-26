package com.xfrog.platform.application.permission;

import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.permission.constant.PermissionOperationLogConstants;
import com.xfrog.platform.application.permission.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.UpdateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.service.OrganizationService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {

    private final OrganizationService organizationService;

    @Authorization("admin:system:organization:create")
    @Override
    @OperationLog(bizId = "#result", bizCode = "#p0.name", bizType = PermissionOperationLogConstants.BIZ_TYPE_ORGANIZATION, bizAction = OperationActionConstants.CREATE)
    public Long createOrganization(CreateOrganizationRequestDTO organization) {
        return organizationService.createOrganization(organization);
    }

    @Authorization(value = "admin:system:organization", demoDisabled = false)
    @Override
    public List<OrganizationDTO> listOrganizations(QueryOrganizationRequestDTO queryDTO) {
        return organizationService.listOrganizations(queryDTO);
    }

    @Authorization(value = "admin:system:organization", demoDisabled = false)
    @Override
    public OrganizationDTO getOrganization(Long organizationId) {
        return organizationService.getOrganization(organizationId);
    }

    @Authorization("admin:system:organization:edit")
    @Override
    @OperationLog(bizId = "#p0", bizCode = "#p1.name", bizType = PermissionOperationLogConstants.BIZ_TYPE_ORGANIZATION, bizAction = OperationActionConstants.UPDATE)
    public void updateOrganization(Long organizationId, UpdateOrganizationRequestDTO organization) {
        organizationService.updateOrganization(organizationId, organization);
    }

    @Authorization("admin:system:organization:delete")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_ORGANIZATION, bizAction = OperationActionConstants.DELETE)
    public void deleteOrganization(Long organizationId) {
        organizationService.deleteOrganization(organizationId);
    }
}
