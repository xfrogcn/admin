package com.xfrog.platform.infrastructure.permission.mapper;

import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.infrastructure.permission.dataobject.RolePO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapperEx<RolePO> {
    List<PermissionItemDTO> queryRolePermissions(@Param("roleId") Long roleId);
}
