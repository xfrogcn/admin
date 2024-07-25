package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.framework.oplog.OpLogMDC;
import com.xfrog.platform.application.base.converter.LangDTOConverter;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTO;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;
import com.xfrog.platform.application.base.repository.LangRepository;
import com.xfrog.platform.application.base.service.LangService;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.base.command.CreateLangCommand;
import com.xfrog.platform.domain.base.command.UpdateLangCommand;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LangServiceImpl implements LangService {
    private final LangDomainRepository languageDomainRepository;
    private final LangRepository languageRepository;
    private final LangCorpusDomainRepository langCorpusDomainRepository;
    private final LangLocalDomainRepository langLocalDomainRepository;

    @Override
    @Transactional
    public Long createLanguage(CreateLangRequestDTO languageRequestDTO) {
        if (languageDomainRepository.existsByCode(languageRequestDTO.getApplication(), languageRequestDTO.getCode())) {
            throw new FailedPreconditionException("language already exists");
        }

        CreateLangCommand createLangCommand = LangDTOConverter.INSTANCE.toCreateCommand(languageRequestDTO);
        Lang lang = Lang.create(createLangCommand);

        Lang savedLang = languageDomainRepository.save(lang);

        fillLanguageCorpusLocal(savedLang.getApplication(), savedLang.getId(), languageRequestDTO.getReferenceLangId());

        return savedLang.getId();
    }

    @Override
    @Transactional
    public void updateLanguage(Long languageId, UpdateLangRequestDTO updateLanguageRequestDTO) {
        Lang lang = languageDomainRepository.findById(languageId);
        if (lang == null) {
            throw new NotFoundException("lang not found");
        }
        OpLogMDC.putBizCode(lang.getCode());
        UpdateLangCommand updateLangCommand = LangDTOConverter.INSTANCE.toUpdateCommand(updateLanguageRequestDTO);
        lang.update(updateLangCommand);

        languageDomainRepository.save(lang);
    }

    @Override
    @Transactional
    public void deleteLanguage(Long languageId) {
        Lang lang = languageDomainRepository.findById(languageId);
        if (lang == null) {
            throw new NotFoundException("lang not found");
        }
        OpLogMDC.putBizCode(lang.getCode());
        languageDomainRepository.logicDelete(languageId);
    }

    @Override
    public PageDTO<LangDTO> listLanguages(QueryLangRequestDTO queryDTO) {
        return languageRepository.queryBy(queryDTO);
    }

    @Override
    public LangDTO getLanguage(Long languageId) {
        return languageRepository.queryById(languageId);
    }

    @Override
    @Transactional
    public void enableLanguage(Long langId, Boolean enabled, Long referenceLangId) {
        Lang lang = languageDomainRepository.findById(langId);
        if (lang == null) {
            throw new NotFoundException("lang not found");
        }
        OpLogMDC.putBizCode(lang.getCode());
        if (enabled) {
            lang.enable();
        } else {
            lang.disable();
        }

        languageDomainRepository.save(lang);

        if (enabled) {
            fillLanguageCorpusLocal(lang.getApplication(), langId, referenceLangId);
        }
    }

    protected void fillLanguageCorpusLocal(String application, Long targetLanguageId, Long referenceLanguageId) {
        // 所有的语料
        List<LangCorpus> langCorpuses = langCorpusDomainRepository.findAllByApplication(application);
        // 所有的本地化语言
        List<Long> langIds = new LinkedList<>();
        langIds.add(targetLanguageId);
        if (referenceLanguageId != null) {
            langIds.add(referenceLanguageId);
        }
        Map<Long, Lang> langMap = languageDomainRepository.findByIds(langIds).stream()
                .collect(Collectors.toMap(Lang::getId, Function.identity()));
        Lang targetLang = langMap.get(targetLanguageId);

        List<LangLocal> langLocals = langLocalDomainRepository.findAllByApplicationAndLangId(application, langIds);

        Map<Long, LangLocal> targetLangLocalMap = langLocals.stream()
                .filter(it -> Objects.equals(it.getLangId(), targetLanguageId))
                .collect(Collectors.toMap(LangLocal::getLangCorpusId, Function.identity()));
        Map<Long, LangLocal> referenceLangLocalMap = referenceLanguageId == null ? new HashMap<>()
                : langLocals.stream()
                .filter(it -> Objects.equals(it.getLangId(), referenceLanguageId))
                .collect(Collectors.toMap(LangLocal::getLangCorpusId, Function.identity()));

        List<LangLocal> newLangLocals = new LinkedList<>();
        langCorpuses.forEach(langCorpus -> {
            LangLocal targetLangLocal = targetLangLocalMap.get(langCorpus.getId());
            if (targetLangLocal != null) {
                return;
            }
            LangLocal referenceLangLocal = referenceLangLocalMap.get(langCorpus.getId());
            if (referenceLangLocal != null) {
                targetLangLocal = LangLocal.createFromReference(referenceLangLocal, targetLang);
            } else {
                targetLangLocal = LangLocal.createFromCorpus(langCorpus, targetLang);
            }
            newLangLocals.add(targetLangLocal);
        });

        if (!newLangLocals.isEmpty()) {
            langLocalDomainRepository.saveAll(newLangLocals);
        }
    }
}
