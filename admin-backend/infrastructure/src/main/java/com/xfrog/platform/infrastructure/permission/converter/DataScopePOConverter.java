package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.platform.application.permission.dto.DataScopeDTO;
import com.xfrog.platform.domain.permission.aggregate.DataScope;
import com.xfrog.platform.infrastructure.permission.dataobject.DataScopePO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataScopePOConverter extends DomainAndPOConverter<DataScope, DataScopePO>,
        POToDTOConverter<DataScopePO, DataScopeDTO> {
    DataScopePOConverter INSTANCE = Mappers.getMapper(DataScopePOConverter.class);
}
