package com.xfrog.framework.repository;

import java.util.List;

public interface ApplicationRepository<DTO> {
    DTO queryById(Long id);

    List<DTO> queryByIds(List<Long> ids);
}
