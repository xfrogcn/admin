package com.xfrog.platform.application.base.converter;

import com.xfrog.framework.converter.DTOToCreateCommandConverter;
import com.xfrog.framework.converter.DTOToUpdateCommandConverter;
import com.xfrog.framework.converter.DomainToDTOConverter;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.command.CreateLangCommand;
import com.xfrog.platform.domain.base.command.UpdateLangCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LangDTOConverter
        extends DTOToCreateCommandConverter<CreateLangRequestDTO, CreateLangCommand>,
        DTOToUpdateCommandConverter<UpdateLangRequestDTO, UpdateLangCommand>,
        DomainToDTOConverter<Lang, LangDTO> {
    LangDTOConverter INSTANCE = Mappers.getMapper(LangDTOConverter.class);
}
