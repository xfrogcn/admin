package com.xfrog.platform.application.base.converter;

import com.xfrog.platform.application.base.dto.CreateDicRequestDTO;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.UpdateDicRequestDTO;
import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.command.CreateDicCommand;
import com.xfrog.platform.domain.base.command.UpdateDicCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DicDTOToCommandConverter {

    DicDTOToCommandConverter INSTANCE = Mappers.getMapper(DicDTOToCommandConverter.class);

    CreateDicCommand requestToCreateCommand(CreateDicRequestDTO requestDTO);

    UpdateDicCommand requestToUpdateCommand(UpdateDicRequestDTO requestDTO);

    DicDTO toDTO(Dic dic);
}