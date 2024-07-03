package com.xfrog.platform.infrastructure.permission.converter;

import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPOConverter extends DomainAndPOConverter<User, UserPO> {
    UserPOConverter INSTANCE = Mappers.getMapper(UserPOConverter.class);

    UserDTO toDTO(UserPO po);

    List<UserDTO> toDTOList(List<UserPO> pos);
}
