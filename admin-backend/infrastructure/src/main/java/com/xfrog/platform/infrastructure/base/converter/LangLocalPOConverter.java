package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.infrastructure.base.dataobject.LangLocalPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LangLocalPOConverter extends DomainAndPOConverter<LangLocal, LangLocalPO> {
    LangLocalPOConverter INSTANCE = Mappers.getMapper(LangLocalPOConverter.class);
}