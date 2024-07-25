package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.base.LangApi;
import com.xfrog.platform.application.base.constant.BaseOperationLogConstants;
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

    @Authorization("admin:platform:lang:create")
    @Override
    @OperationLog(bizId = "#return", bizCode = "#p0.application + '-' + #p0.code", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG, bizAction = OperationActionConstants.CREATE)
    public Long createLanguage(CreateLangRequestDTO language) {
        return languageService.createLanguage(language);
    }

    @Authorization(value = "admin:platform:lang", demoDisabled = false)
    @Override
    public PageDTO<LangDTO> listLanguages(QueryLangRequestDTO queryDTO) {
        return languageService.listLanguages(queryDTO);
    }

    @Authorization(value = "admin:platform:lang", demoDisabled = false)
    @Override
    public LangDTO getLanguage(Long languageId) {
        return languageService.getLanguage(languageId);
    }

    @Authorization("admin:platform:lang:edit")
    @Override
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG, bizAction = OperationActionConstants.UPDATE)
    public void updateLanguage(Long languageId, UpdateLangRequestDTO language) {
        languageService.updateLanguage(languageId, language);
    }

    @Override
    @Authorization("admin:platform:lang:enable")
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG,
            bizActionSpel = "#p1 ? '" + OperationActionConstants.ENABLE + "': '" +OperationActionConstants.DISABLE + "'")
    public void enableLanguage(Long langId, Boolean enabled, Long referenceLangId) {
        languageService.enableLanguage(langId, enabled, referenceLangId);
    }

    @Authorization("admin:platform:lang:delete")
    @Override
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG, bizAction = OperationActionConstants.DELETE)
    public void deleteLanguage(Long languageId) {
        languageService.deleteLanguage(languageId);
    }
}
