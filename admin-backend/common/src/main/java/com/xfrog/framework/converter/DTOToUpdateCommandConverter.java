package com.xfrog.framework.converter;


import java.util.List;

public interface DTOToUpdateCommandConverter<UPDATE_DTO, UPDATE_COMMAND> {
    UPDATE_COMMAND toUpdateCommand(UPDATE_DTO dto);

    List<UPDATE_COMMAND> toUpdateCommandList(List<UPDATE_DTO> dtos);
}
