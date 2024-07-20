package com.xfrog.framework.repository;

import com.xfrog.framework.dto.IdDTO;

public interface CacheableApplicationRepository<DTO extends IdDTO> extends ApplicationRepository<DTO>, CacheableRepository {
}
