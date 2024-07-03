package com.xfrog.framework.converter;


import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public interface DomainToDTOConverter<DTO, DOMAIN> {
    DTO toDTO(DOMAIN domain);
    default List<DTO> toDTOList(List<DOMAIN> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return new LinkedList<>();
        }
        return domains.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
