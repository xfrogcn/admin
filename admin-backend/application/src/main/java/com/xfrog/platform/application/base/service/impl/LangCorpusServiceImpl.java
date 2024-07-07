package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.repository.LangCorpusRepository;
import com.xfrog.platform.application.base.service.LangCorpusService;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LangCorpusServiceImpl implements LangCorpusService {
    private final LangCorpusDomainRepository langCorpusDomainRepository;
    private final LangCorpusRepository langCorpusRepository;

    @Override
    @Transactional
    public Long createLangCorpus(CreateLangCorpusRequestDTO langCorpusRequestDTO) {
        // Implementation
        return null;
    }

    @Override
    @Transactional
    public void updateLangCorpus(Long langCorpusId, UpdateLangCorpusRequestDTO updateLangCorpusRequestDTO) {
        // Implementation
    }

    @Override
    @Transactional
    public void deleteLangCorpus(Long langCorpusId) {
        langCorpusDomainRepository.logicDelete(langCorpusId);
    }

    @Override
    public PageDTO<LangCorpusDTO> listLangCorpus(QueryLangCorpusRequestDTO queryDTO) {
        return langCorpusRepository.queryAll(queryDTO);
    }

    @Override
    public void enableLangCorpus(Long langCorpusId, Boolean enabled) {

    }

    @Override
    public LangCorpusDTO getLangCorpus(Long langCorpusId) {
        // Implementation
        return null;
    }
}