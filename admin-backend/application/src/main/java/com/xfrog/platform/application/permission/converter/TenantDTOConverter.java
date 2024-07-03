package com.xfrog.platform.application.permission.converter;

import com.xfrog.platform.application.permission.api.dto.CreateTenantRequestDTO;
import com.xfrog.platform.domain.permission.command.CreateTenantCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TenantDTOConverter {
    TenantDTOConverter INSTANCE = Mappers.getMapper(TenantDTOConverter.class);

    CreateTenantCommand toCommand(CreateTenantRequestDTO requestDTO);
}
