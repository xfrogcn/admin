package com.xfrog.platform.application.permission.repository;

import com.xfrog.framework.repository.CacheablePageableApplicationRepository;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;

public interface TenantRepository extends CacheablePageableApplicationRepository<TenantDTO, QueryTenantRequestDTO> {
    TenantDTO queryByCode(String code);

    void removeCacheByCode(String code);
}
