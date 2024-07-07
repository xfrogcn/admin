package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.infrastructure.base.dataobject.DicItemPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DicItemPOConverter
        extends DomainAndPOConverter<DicItem, DicItemPO>, POToDTOConverter<DicItemPO, DicItemDTO> {
    DicItemPOConverter INSTANCE = Mappers.getMapper(DicItemPOConverter.class);
}
