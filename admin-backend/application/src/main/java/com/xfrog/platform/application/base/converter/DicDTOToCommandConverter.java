package com.xfrog.platform.application.base.converter;

import com.xfrog.framework.converter.DTOToCreateCommandConverter;
import com.xfrog.framework.converter.DTOToUpdateCommandConverter;
import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.domain.base.command.CreateDicCommand;
import com.xfrog.platform.domain.base.command.UpdateDicCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DicDTOToCommandConverter
        extends DTOToCreateCommandConverter<CreateDicRequestDTO, CreateDicCommand>,
        DTOToUpdateCommandConverter<UpdateDicRequestDTO, UpdateDicCommand> {
    DicDTOToCommandConverter INSTANCE = Mappers.getMapper(DicDTOToCommandConverter.class);
}