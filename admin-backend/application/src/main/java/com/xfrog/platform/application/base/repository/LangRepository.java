package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;

import java.util.List;

public interface LangRepository extends PageableApplicationRepository<LangDTO, QueryLangRequestDTO> {
    List<LangDTO> queryAllByApplication(String application, boolean enabled);
}
