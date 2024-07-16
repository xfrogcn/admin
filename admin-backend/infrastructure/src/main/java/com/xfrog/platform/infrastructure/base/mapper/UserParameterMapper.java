package com.xfrog.platform.infrastructure.base.mapper;

import com.xfrog.platform.infrastructure.base.dataobject.UserParameterPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserParameterMapper extends BaseMapperEx<UserParameterPO> {
}
