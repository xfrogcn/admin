package com.xfrog.platform.application.base.repository;

import com.xfrog.platform.application.base.dto.LangLocalDTO;

import java.util.List;

public interface LangLocalRepository {
    List<LangLocalDTO> queryByLangCorpusId(Long langCorpusId);
}
