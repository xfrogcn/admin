package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.infrastructure.permission.dataobject.RolePermissionItemPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolePermissionItemPOConverter extends DomainAndPOConverter<RolePermissionItem, RolePermissionItemPO> {
    RolePermissionItemPOConverter INSTANCE = Mappers.getMapper(RolePermissionItemPOConverter.class);
}
