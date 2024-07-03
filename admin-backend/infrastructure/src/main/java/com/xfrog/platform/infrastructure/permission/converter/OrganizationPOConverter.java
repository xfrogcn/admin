package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.infrastructure.permission.dataobject.OrganizationPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationPOConverter extends DomainAndPOConverter<Organization, OrganizationPO> {
    OrganizationPOConverter INSTANCE = Mappers.getMapper(OrganizationPOConverter.class);

    OrganizationDTO toDTO(OrganizationPO organizationPO);

    List<OrganizationDTO> toDTOList(List<OrganizationPO> organizationPOS);
}
