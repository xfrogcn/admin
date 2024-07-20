package com.xfrog.framework.repository;

import com.xfrog.framework.dto.IdDTO;

import java.util.List;

public interface ApplicationRepository<DTO extends IdDTO> {
    DTO queryById(Long id);

    List<DTO> queryByIds(List<Long> ids);
}
