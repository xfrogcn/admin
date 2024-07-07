package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.LangApi;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;
import com.xfrog.platform.application.base.service.LangService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LangController implements LangApi {
    private final LangService languageService;

    @Authorization("admin:platform:language:create")
    @Override
    public Long createLanguage(CreateLangRequestDTO language) {
        return languageService.createLanguage(language);
    }

    @Authorization(value = "admin:platform:language", demoDisabled = false)
    @Override
    public PageDTO<LangDTO> listLanguages(QueryLangRequestDTO queryDTO) {
        return languageService.listLanguages(queryDTO);
    }

    @Authorization(value = "admin:platform:language", demoDisabled = false)
    @Override
    public LangDTO getLanguage(Long languageId) {
        return languageService.getLanguage(languageId);
    }

    @Authorization("admin:platform:language:edit")
    @Override
    public void updateLanguage(Long languageId, UpdateLangRequestDTO language) {
        languageService.updateLanguage(languageId, language);
    }

    @Override
    @Authorization("admin:platform:language:enable")
    public void enableLanguage(Long langId, Boolean enabled, Long referenceLangId) {
        languageService.enableLanguage(langId, enabled, referenceLangId);
    }

    @Authorization("admin:platform:language:delete")
    @Override
    public void deleteLanguage(Long languageId) {
        languageService.deleteLanguage(languageId);
    }
}
