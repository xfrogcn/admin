package com.xfrog.platform.application.permission.converter;

import com.xfrog.framework.converter.DomainToDTOConverter;
import com.xfrog.platform.application.permission.dto.DataScopeDTO;
import com.xfrog.platform.domain.permission.aggregate.DataScope;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataScopeDTOConverter extends DomainToDTOConverter<DataScope, DataScopeDTO> {
    DataScopeDTOConverter INSTANCE = Mappers.getMapper(DataScopeDTOConverter.class);
}
