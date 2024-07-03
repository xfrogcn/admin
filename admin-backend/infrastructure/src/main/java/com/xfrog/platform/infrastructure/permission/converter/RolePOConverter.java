package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.domain.permission.aggregate.Role;
import com.xfrog.platform.infrastructure.permission.dataobject.RolePO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RolePOConverter extends DomainAndPOConverter<Role, RolePO> {
    RolePOConverter INSTANCE = Mappers.getMapper(RolePOConverter.class);

    RoleDTO toDTO(RolePO permissionItemPO);

    List<RoleDTO> toDTOList(List<RolePO> permissionItemPOS);
}