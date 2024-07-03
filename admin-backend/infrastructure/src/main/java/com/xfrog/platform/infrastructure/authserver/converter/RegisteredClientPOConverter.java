package com.xfrog.platform.infrastructure.authserver.converter;


import com.xfrog.platform.application.authserver.dto.RegisteredClientDTO;
import com.xfrog.platform.infrastructure.authserver.dataobject.RegisteredClientPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class RegisteredClientPOConverter {

    public static RegisteredClientPOConverter INSTANCE = Mappers.getMapper(RegisteredClientPOConverter.class);

    public abstract RegisteredClientPO toPO(RegisteredClientDTO registeredClientDTO);

    public abstract RegisteredClientDTO toDTO(RegisteredClientPO registeredClientPO);
}
