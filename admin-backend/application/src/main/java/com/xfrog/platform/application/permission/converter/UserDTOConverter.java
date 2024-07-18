package com.xfrog.platform.application.permission.converter;

import com.xfrog.framework.converter.DomainToDTOConverter;
import com.xfrog.platform.application.permission.api.dto.CurrentUserInfoDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.domain.permission.aggregate.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDTOConverter extends DomainToDTOConverter<User, UserDTO> {
    UserDTOConverter INSTANCE = Mappers.getMapper(UserDTOConverter.class);

    CurrentUserInfoDTO toCurrentUser(UserDTO user);
}
