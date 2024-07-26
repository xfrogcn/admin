package com.xfrog.platform.application.permission.converter;

import com.xfrog.framework.converter.DomainToDTOConverter;
import com.xfrog.platform.application.permission.dto.PermissionItemDTO;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionItemDTOConverter extends DomainToDTOConverter<PermissionItemDTO, PermissionItem> {
    PermissionItemDTOConverter INSTANCE = Mappers.getMapper(PermissionItemDTOConverter.class);
}
