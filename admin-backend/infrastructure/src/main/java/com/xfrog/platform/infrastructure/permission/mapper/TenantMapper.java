package com.xfrog.platform.infrastructure.permission.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;
import com.xfrog.platform.infrastructure.permission.dataobject.TenantPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenantMapper extends BaseMapperEx<TenantPO> {
    List<TenantDTO> queryAllBy(@Param("queryDTO") QueryTenantRequestDTO queryDTO, @Param("page") Page<TenantDTO> page);
}
