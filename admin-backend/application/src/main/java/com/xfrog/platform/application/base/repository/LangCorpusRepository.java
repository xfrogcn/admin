package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;

public interface LangCorpusRepository {
    PageDTO<LangCorpusDTO> queryAll(QueryLangCorpusRequestDTO queryDTO);

    LangCorpusDTO queryById(Long id);
}
