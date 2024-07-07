package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.infrastructure.base.dataobject.LangCorpusPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LangCorpusPOConverter extends DomainAndPOConverter<LangCorpus, LangCorpusPO>, POToDTOConverter<LangCorpusPO, LangCorpusDTO> {
    LangCorpusPOConverter INSTANCE = Mappers.getMapper(LangCorpusPOConverter.class);
}
