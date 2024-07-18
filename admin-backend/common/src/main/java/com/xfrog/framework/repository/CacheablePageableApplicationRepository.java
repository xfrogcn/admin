package com.xfrog.framework.repository;

import com.xfrog.framework.dto.PageQueryDTO;

public interface CacheablePageableApplicationRepository<DTO, QueryDTO extends PageQueryDTO> extends PageableApplicationRepository<DTO, QueryDTO>,
        CacheableRepository {
}
