package com.xfrog.platform.application.base.service;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;

import java.util.List;

public interface LangCorpusService {
    List<Long> createLangCorpus(CreateLangCorpusRequestDTO langCorpus);

    void updateLangCorpus(Long langCorpusId, UpdateLangCorpusRequestDTO langCorpus);

    void deleteLangCorpus(Long langCorpusId);

    PageDTO<LangCorpusDTO> listLangCorpus(QueryLangCorpusRequestDTO queryDTO);

    void enableLangCorpus(Long langCorpusId, Boolean enabled);


    LangCorpusDTO getLangCorpus(Long langCorpusId);
}
