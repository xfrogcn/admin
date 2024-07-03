package com.xfrog.platform.infrastructure.authserver.mapper;

import com.xfrog.platform.infrastructure.authserver.dataobject.OAuth2SessionPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2SessionsMapper extends BaseMapperEx<OAuth2SessionPO> {
}
