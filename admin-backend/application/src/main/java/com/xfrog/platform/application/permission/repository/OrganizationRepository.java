package com.xfrog.platform.application.permission.repository;


import com.xfrog.framework.repository.ApplicationRepository;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;

import java.util.List;

public interface OrganizationRepository extends ApplicationRepository<OrganizationDTO> {
    List<OrganizationDTO> queryBy(QueryOrganizationRequestDTO queryDTO);
}
