package com.xfrog.platform.application.permission.repository;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;

public interface TenantRepository {
    PageDTO<TenantDTO> queryAllBy(QueryTenantRequestDTO queryDTO);
}
