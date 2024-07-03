package com.xfrog.platform.application.permission.api;

import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateOrganizationRequestDTO;
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
    public void updateOrganization(Long organizationId, UpdateOrganizationRequestDTO organization) {
        organizationService.updateOrganization(organizationId, organization);
    }

    @Authorization("admin:system:organization:delete")
    @Override
    public void deleteOrganization(Long organizationId) {
        organizationService.deleteOrganization(organizationId);
    }
}
