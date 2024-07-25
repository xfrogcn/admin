package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.base.LangCorpusApi;
import com.xfrog.platform.application.base.constant.BaseOperationLogConstants;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.service.LangCorpusService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LangCorpusController implements LangCorpusApi {
    private final LangCorpusService langCorpusService;

    @Authorization("admin:platform:langcorpus:create")
    @Override
    @OperationLog(bizId = "#return", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG_CORPUS, bizAction = OperationActionConstants.CREATE)
    public List<Long> createLangCorpus(CreateLangCorpusRequestDTO langCorpus) {
        return langCorpusService.createLangCorpus(langCorpus);
    }

    @Authorization(value = "admin:platform:langcorpus", demoDisabled = false)
    @Override
    public PageDTO<LangCorpusDTO> listLangCorpus(QueryLangCorpusRequestDTO queryDTO) {
        return langCorpusService.listLangCorpus(queryDTO);
    }

    @Authorization(value = "admin:platform:langcorpus", demoDisabled = false)
    @Override
    public LangCorpusDTO getLangCorpus(Long langCorpusId) {
        return langCorpusService.getLangCorpus(langCorpusId);
    }

    @Authorization("admin:platform:langcorpus:edit")
    @Override
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG_CORPUS, bizAction = OperationActionConstants.UPDATE)
    public void updateLangCorpus(Long langCorpusId, UpdateLangCorpusRequestDTO langCorpus) {
        langCorpusService.updateLangCorpus(langCorpusId, langCorpus);
    }

    @Authorization("admin:platform:langcorpus:enable")
    @Override
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG_CORPUS,
            bizAction = "#p1 ? '" + OperationActionConstants.ENABLE + "': '" +OperationActionConstants.DISABLE + "'")
    public void enableLangCorpus(Long langCorpusId, Boolean enabled) {
        langCorpusService.enableLangCorpus(langCorpusId, enabled);
    }

    @Authorization("admin:platform:langcorpus:delete")
    @Override
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG_CORPUS, bizAction = OperationActionConstants.DELETE)
    public void deleteLangCorpus(Long langCorpusId) {
        langCorpusService.deleteLangCorpus(langCorpusId);
    }

    @Authorization("admin:platform:langcorpus:local")
    @Override
    @OperationLog(bizId = "#p0", bizType = BaseOperationLogConstants.BIZ_TYPE_LANG_LOCALE, bizAction = OperationActionConstants.UPDATE)
    public void configLangLocal(Long langCorpusId, Map<String, String> langLocal) {
        langCorpusService.configLangLocal(langCorpusId, langLocal);
    }

    @Override
    public Map<String, String> getLangLocal(String application, String langCode) {
        return langCorpusService.getLangLocal(application, langCode);
    }
}
