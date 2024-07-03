package com.xfrog.platform.infrastructure.permission.mapper;

import com.xfrog.platform.infrastructure.permission.dataobject.UserRolePO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapperEx<UserRolePO> {
}
