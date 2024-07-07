package com.xfrog.platform.application.base.converter;

import com.xfrog.framework.converter.DTOToCreateCommandConverter;
import com.xfrog.framework.converter.DTOToUpdateCommandConverter;
import com.xfrog.framework.converter.DomainToDTOConverter;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.command.CreateLangCorpusCommand;
import com.xfrog.platform.domain.base.command.UpdateLangCorpusCommand;
import org.mapstruct.factory.Mappers;

public interface LangCorpusDTOConverter
        extends DTOToCreateCommandConverter<CreateLangCorpusRequestDTO, CreateLangCorpusCommand>,
        DTOToUpdateCommandConverter<UpdateLangCorpusRequestDTO, UpdateLangCorpusCommand>,
        DomainToDTOConverter<LangCorpus, LangCorpusDTO> {

    LangCorpusDTOConverter INSTANCE = Mappers.getMapper(LangCorpusDTOConverter.class);

}
