package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;

public interface LangRepository extends PageableApplicationRepository<LangDTO, QueryLangRequestDTO> {

}
