package com.xfrog.platform.infrastructure.base.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.domain.base.aggregate.UserParameter;
import com.xfrog.platform.infrastructure.base.dataobject.UserParameterPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserParameterPOConverter extends DomainAndPOConverter<UserParameter, UserParameterPO> {
    UserParameterPOConverter INSTANCE = Mappers.getMapper(UserParameterPOConverter.class);
}
