package com.xfrog.platform.application.permission.repository;


import com.xfrog.framework.repository.CacheableApplicationRepository;
import com.xfrog.platform.application.permission.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.dto.QueryOrganizationRequestDTO;

import java.util.List;

public interface OrganizationRepository extends CacheableApplicationRepository<OrganizationDTO> {
    List<OrganizationDTO> queryBy(QueryOrganizationRequestDTO queryDTO);
}
