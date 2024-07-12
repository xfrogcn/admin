package com.xfrog.platform.infrastructure.permission.mapper;

import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.QueryOrganizationRequestDTO;
import com.xfrog.platform.infrastructure.permission.dataobject.OrganizationPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrganizationMapper extends BaseMapperEx<OrganizationPO> {
    List<OrganizationDTO> queryBy(@Param("queryDTO") QueryOrganizationRequestDTO queryDTO);
}
