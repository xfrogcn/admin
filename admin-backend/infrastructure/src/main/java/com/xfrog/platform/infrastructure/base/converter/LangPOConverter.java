package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.infrastructure.base.dataobject.LangPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LangPOConverter extends DomainAndPOConverter<Lang, LangPO> {
    LangPOConverter INSTANCE = Mappers.getMapper(LangPOConverter.class);
}
