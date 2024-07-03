package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.infrastructure.permission.dataobject.UserRolePO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRolePOConverter extends DomainAndPOConverter<UserRole, UserRolePO> {
    UserRolePOConverter INSTANCE = Mappers.getMapper(UserRolePOConverter.class);
}
