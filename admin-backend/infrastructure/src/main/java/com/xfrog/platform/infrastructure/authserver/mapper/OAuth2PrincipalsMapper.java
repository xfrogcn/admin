package com.xfrog.platform.infrastructure.authserver.mapper;

import com.xfrog.platform.infrastructure.authserver.dataobject.OAuth2PrincipalPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2PrincipalsMapper extends BaseMapperEx<OAuth2PrincipalPO> {
}
