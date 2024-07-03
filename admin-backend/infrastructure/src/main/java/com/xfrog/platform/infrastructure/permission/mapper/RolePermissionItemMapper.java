package com.xfrog.platform.infrastructure.permission.mapper;

import com.xfrog.platform.infrastructure.permission.dataobject.RolePermissionItemPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionItemMapper extends BaseMapperEx<RolePermissionItemPO> {
}