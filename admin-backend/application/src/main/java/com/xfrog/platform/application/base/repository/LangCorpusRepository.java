package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;

public interface LangCorpusRepository extends PageableApplicationRepository<LangCorpusDTO, QueryLangCorpusRequestDTO> {
    
}
