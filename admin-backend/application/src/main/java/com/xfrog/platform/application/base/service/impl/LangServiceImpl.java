package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;
import com.xfrog.platform.application.base.repository.LangRepository;
import com.xfrog.platform.application.base.service.LangService;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LangServiceImpl implements LangService {
    private final LangDomainRepository languageDomainRepository;
    private final LangRepository languageRepository;

    @Override
    @Transactional
    public Long createLanguage(CreateLangRequestDTO languageRequestDTO) {
        // Implementation
        return null;
    }

    @Override
    @Transactional
    public void updateLanguage(Long languageId, UpdateLangRequestDTO updateLanguageRequestDTO) {
        // Implementation
    }

    @Override
    @Transactional
    public void deleteLanguage(Long languageId) {
        languageDomainRepository.logicDelete(languageId);
    }

    @Override
    public PageDTO<LangDTO> listLanguages(QueryLangRequestDTO queryDTO) {
        return languageRepository.queryAll(queryDTO);
    }

    @Override
    public LangDTO getLanguage(Long languageId) {
        // Implementation
        return null;
    }

    @Override
    public void enableLanguage(Long langId, Boolean enabled, Long referenceLangId) {

    }
}
