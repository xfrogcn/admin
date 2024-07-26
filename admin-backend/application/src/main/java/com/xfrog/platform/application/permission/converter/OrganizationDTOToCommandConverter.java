package com.xfrog.platform.application.permission.converter;


import com.xfrog.framework.converter.DTOToCreateCommandConverter;
import com.xfrog.framework.converter.DTOToUpdateCommandConverter;
import com.xfrog.framework.converter.DomainToDTOConverter;
import com.xfrog.platform.application.permission.dto.CreateOrganizationRequestDTO;
import com.xfrog.platform.application.permission.dto.OrganizationDTO;
import com.xfrog.platform.application.permission.dto.UpdateOrganizationRequestDTO;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.command.CreateOrganizationCommand;
import com.xfrog.platform.domain.permission.command.UpdateOrganizationCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationDTOToCommandConverter
        extends DTOToCreateCommandConverter<CreateOrganizationRequestDTO, CreateOrganizationCommand>,
        DTOToUpdateCommandConverter<UpdateOrganizationRequestDTO, UpdateOrganizationCommand>,
        DomainToDTOConverter<Organization, OrganizationDTO> {

    OrganizationDTOToCommandConverter INSTANCE = Mappers.getMapper(OrganizationDTOToCommandConverter.class);

}
