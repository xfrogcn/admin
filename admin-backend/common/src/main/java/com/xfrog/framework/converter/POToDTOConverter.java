package com.xfrog.framework.converter;

import java.util.List;

public interface POToDTOConverter<PO, DTO> {
    DTO toDTO(PO po);

    List<DTO> toDTOList(List<PO> pos);
}
