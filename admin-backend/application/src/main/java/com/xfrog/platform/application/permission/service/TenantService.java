package com.xfrog.platform.application.permission.service;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.dto.CreateTenantRequestDTO;
import com.xfrog.platform.application.permission.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.dto.TenantDTO;
import com.xfrog.platform.application.permission.dto.UpdateTenantRequestDTO;

public interface TenantService {

    Long createTenant(CreateTenantRequestDTO tenantDTO);

    PageDTO<TenantDTO> listTenants(QueryTenantRequestDTO requestDTO);

    void updateTenant(Long tenantId, UpdateTenantRequestDTO tenantDTO);

    void enableTenant(Long tenantId, Boolean enabled);
}
