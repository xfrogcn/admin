package com.xfrog.platform.infrastructure.permission.mapper;

import com.xfrog.platform.infrastructure.permission.dataobject.PermissionItemPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionItemMapper extends BaseMapperEx<PermissionItemPO> {
}
