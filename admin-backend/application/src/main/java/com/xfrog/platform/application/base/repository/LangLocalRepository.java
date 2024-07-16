package com.xfrog.platform.application.base.repository;

import com.xfrog.platform.application.base.dto.LangLocalDTO;

import java.util.List;
import java.util.Map;

public interface LangLocalRepository {
    List<LangLocalDTO> queryByLangCorpusId(Long langCorpusId);

    Map<String, String> queryByApplicationAndLangCode(String application, String langCode);
}
