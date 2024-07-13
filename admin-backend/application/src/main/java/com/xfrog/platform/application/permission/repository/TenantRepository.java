package com.xfrog.platform.application.permission.repository;

import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;

public interface TenantRepository extends PageableApplicationRepository<TenantDTO, QueryTenantRequestDTO> {
    TenantDTO queryByCode(String code);
}
