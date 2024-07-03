package com.xfrog.framework.converter;


import java.util.List;

public interface DTOToCommandConverter<DTO, COMMAND> {
    COMMAND toCommand(DTO dto);

    List<COMMAND> toCommandList(List<DTO> dtos);
}
