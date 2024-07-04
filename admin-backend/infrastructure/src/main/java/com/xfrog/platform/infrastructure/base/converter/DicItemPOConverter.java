package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.infrastructure.base.dataobject.DicItemPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DicItemPOConverter extends DomainAndPOConverter<DicItem, DicItemPO> {
    DicItemPOConverter INSTANCE = Mappers.getMapper(DicItemPOConverter.class);

    DicItemDTO toDTO(DicItemPO dicItemPO);

    List<DicItemDTO> toDTOList(List<DicItemPO> dicItemPOS);
}
