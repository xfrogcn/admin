package com.xfrog.framework.repository;

import com.xfrog.framework.dto.IdDTO;
import com.xfrog.framework.dto.PageQueryDTO;

public interface CacheablePageableApplicationRepository<DTO extends IdDTO, QueryDTO extends PageQueryDTO> extends PageableApplicationRepository<DTO, QueryDTO>,
        CacheableRepository {
}
