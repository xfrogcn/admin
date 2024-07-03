package com.xfrog.platform.application.permission.converter;


import com.xfrog.platform.application.permission.api.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.api.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateOrganizationRequestDTO;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.command.CreateOrganizationCommand;
import com.xfrog.platform.domain.permission.command.UpdateOrganizationCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationDTOToCommandConverter {

    OrganizationDTOToCommandConverter INSTANCE = Mappers.getMapper(OrganizationDTOToCommandConverter.class);

    CreateOrganizationCommand requestToCreateCommand(CreateOrganizationRequestDTO requestDTO);

    UpdateOrganizationCommand requestToUpdateCommand(UpdateOrganizationRequestDTO requestDTO);

    OrganizationDTO toDTO(Organization organization);
}
