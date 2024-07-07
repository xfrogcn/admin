package com.xfrog.framework.converter;


import java.util.List;

public interface DTOToCreateCommandConverter<CREATE_DTO, CREATE_COMMAND> {
    CREATE_COMMAND toCreateCommand(CREATE_DTO dto);

    List<CREATE_COMMAND> toCreateCommandList(List<CREATE_DTO> dtos);
}
