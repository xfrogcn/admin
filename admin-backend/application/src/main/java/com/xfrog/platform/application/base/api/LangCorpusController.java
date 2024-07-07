package com.xfrog.platform.application.base.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.base.LangCorpusApi;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.service.LangCorpusService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LangCorpusController implements LangCorpusApi {
    private final LangCorpusService langCorpusService;

    @Authorization("admin:system:langcorpus:create")
    @Override
    public Long createLangCorpus(CreateLangCorpusRequestDTO langCorpus) {
        return langCorpusService.createLangCorpus(langCorpus);
    }

    @Authorization(value = "admin:system:langcorpus", demoDisabled = false)
    @Override
    public PageDTO<LangCorpusDTO> listLangCorpus(QueryLangCorpusRequestDTO queryDTO) {
        return langCorpusService.listLangCorpus(queryDTO);
    }

    @Authorization(value = "admin:system:langcorpus", demoDisabled = false)
    @Override
    public LangCorpusDTO getLangCorpus(Long langCorpusId) {
        return langCorpusService.getLangCorpus(langCorpusId);
    }

    @Authorization("admin:system:langcorpus:edit")
    @Override
    public void updateLangCorpus(Long langCorpusId, UpdateLangCorpusRequestDTO langCorpus) {
        langCorpusService.updateLangCorpus(langCorpusId, langCorpus);
    }

    @Authorization("admin:system:langcorpus:enable")
    @Override
    public void enableLangCorpus(Long langCorpusId, Boolean enabled) {
        langCorpusService.enableLangCorpus(langCorpusId, enabled);
    }

    @Authorization("admin:system:langcorpus:delete")
    @Override
    public void deleteLangCorpus(Long langCorpusId) {
        langCorpusService.deleteLangCorpus(langCorpusId);
    }
}
