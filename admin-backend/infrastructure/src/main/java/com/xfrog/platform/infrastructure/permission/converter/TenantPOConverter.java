package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.platform.application.permission.dto.TenantDTO;
import com.xfrog.platform.domain.permission.aggregate.Tenant;
import com.xfrog.platform.infrastructure.permission.dataobject.TenantPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantPOConverter extends DomainAndPOConverter<Tenant, TenantPO>,
        POToDTOConverter<TenantPO, TenantDTO> {
    TenantPOConverter INSTANCE = Mappers.getMapper(TenantPOConverter.class);
}