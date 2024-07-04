package com.xfrog.platform.application.base.converter;

import com.xfrog.platform.application.base.dto.CreateDicItemRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateDicItemRequestDTO;
import com.xfrog.platform.domain.base.command.CreateDicItemCommand;
import com.xfrog.platform.domain.base.command.UpdateDicItemCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DicItemDTOToCommandConverter {

    DicItemDTOToCommandConverter INSTANCE = Mappers.getMapper(DicItemDTOToCommandConverter.class);

    CreateDicItemCommand requestToCreateCommand(CreateDicItemRequestDTO requestDTO);

    UpdateDicItemCommand requestToUpdateCommand(UpdateDicItemRequestDTO requestDTO);
}