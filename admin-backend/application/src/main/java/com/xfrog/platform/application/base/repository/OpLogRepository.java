package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.application.base.dto.OpLogDTO;
import com.xfrog.platform.application.base.dto.QueryOpLogRequestDTO;

public interface OpLogRepository extends PageableApplicationRepository<OpLogDTO, QueryOpLogRequestDTO> {
}
