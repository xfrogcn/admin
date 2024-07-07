package com.xfrog.platform.application.base.service;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;

public interface LangService {
    Long createLanguage(CreateLangRequestDTO language);

    void updateLanguage(Long languageId, UpdateLangRequestDTO language);

    void deleteLanguage(Long languageId);

    PageDTO<LangDTO> listLanguages(QueryLangRequestDTO queryDTO);

    LangDTO getLanguage(Long languageId);

    void enableLanguage(Long langId, Boolean enabled, Long referenceLangId);

}
