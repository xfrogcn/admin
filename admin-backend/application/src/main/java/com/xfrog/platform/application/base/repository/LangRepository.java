package com.xfrog.platform.application.base.repository;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;

public interface LangRepository {
    PageDTO<LangDTO> queryAll(QueryLangRequestDTO queryDTO);

    LangDTO queryById(Long id);
}
