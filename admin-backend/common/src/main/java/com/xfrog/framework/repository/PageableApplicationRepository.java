package com.xfrog.framework.repository;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.dto.PageQueryDTO;

public interface PageableApplicationRepository<DTO, QueryDTO extends PageQueryDTO> extends ApplicationRepository<DTO> {
    PageDTO<DTO> queryBy(QueryDTO queryDTO);
}
