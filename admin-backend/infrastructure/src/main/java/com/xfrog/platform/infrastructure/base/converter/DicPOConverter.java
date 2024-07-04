package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.infrastructure.base.dataobject.DicPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DicPOConverter extends DomainAndPOConverter<Dic, DicPO> {
    DicPOConverter INSTANCE = Mappers.getMapper(DicPOConverter.class);

    DicDTO toDTO(DicPO dicPO);

    List<DicDTO> toDTOList(List<DicPO> dicPOS);
}
