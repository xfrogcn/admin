package com.xfrog.platform.infrastructure.authserver.mapper;

import com.xfrog.platform.infrastructure.authserver.dataobject.RegisteredClientPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientsMapper extends BaseMapperEx<RegisteredClientPO> {
}
