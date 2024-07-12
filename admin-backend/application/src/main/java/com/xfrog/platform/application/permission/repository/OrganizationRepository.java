package com.xfrog.platform.application.permission.repository;


import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;

import java.util.List;

public interface OrganizationRepository {
    List<OrganizationDTO> queryAll(QueryOrganizationRequestDTO queryDTO);

    OrganizationDTO queryById(Long organizationId);

    List<OrganizationDTO> queryByIds(List<Long> organizationIds);
}
