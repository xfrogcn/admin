package com.xfrog.platform.infrastructure.permission.mapper;

import com.xfrog.platform.application.permission.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.dto.TenantDTO;
import com.xfrog.platform.infrastructure.permission.dataobject.TenantPO;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper extends PageableMapper<TenantPO, TenantDTO, QueryTenantRequestDTO> {

}
