package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.infrastructure.permission.dataobject.OrganizationPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationPOConverter extends DomainAndPOConverter<Organization, OrganizationPO>,
        POToDTOConverter<OrganizationPO, OrganizationDTO> {
    OrganizationPOConverter INSTANCE = Mappers.getMapper(OrganizationPOConverter.class);

}
