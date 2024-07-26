package com.xfrog.platform.application.permission.service;


import com.xfrog.platform.application.permission.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.UpdateOrganizationRequestDTO;

import java.util.List;

public interface OrganizationService {
    Long createOrganization(CreateOrganizationRequestDTO organization);

    void updateOrganization(Long organizationId, UpdateOrganizationRequestDTO organization);

    void deleteOrganization(Long organizationId);

    List<OrganizationDTO> listOrganizations(QueryOrganizationRequestDTO queryDTO);

    OrganizationDTO getOrganization(Long organizationId);
}
