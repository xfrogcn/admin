package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import com.xfrog.platform.infrastructure.permission.dataobject.PermissionItemPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionItemPOConverter extends DomainAndPOConverter<PermissionItem, PermissionItemPO> {
    PermissionItemPOConverter INSTANCE = Mappers.getMapper(PermissionItemPOConverter.class);

    PermissionItemDTO toDTO(PermissionItemPO permissionItemPO);

    List<PermissionItemDTO> toDTOList(List<PermissionItemPO> permissionItemPOS);
}
