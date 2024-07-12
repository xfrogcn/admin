package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPOConverter extends DomainAndPOConverter<User, UserPO>,
        POToDTOConverter<UserPO, UserDTO> {
    UserPOConverter INSTANCE = Mappers.getMapper(UserPOConverter.class);

}
