package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.domain.IdEntity;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.framework.oplog.OpLogMDC;
import com.xfrog.platform.application.base.converter.LangCorpusDTOConverter;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangCorpusDTO;
import com.xfrog.platform.application.base.dto.LangCorpusItemDTO;
import com.xfrog.platform.application.base.dto.LangLocalDTO;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.repository.LangCorpusRepository;
import com.xfrog.platform.application.base.repository.LangLocalRepository;
import com.xfrog.platform.application.base.service.LangCorpusService;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.base.command.CreateLangCorpusCommand;
import com.xfrog.platform.domain.base.command.UpdateLangCorpusCommand;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LangCorpusServiceImpl implements LangCorpusService {
    private final LangCorpusDomainRepository langCorpusDomainRepository;
    private final LangCorpusRepository langCorpusRepository;
    private final LangDomainRepository langDomainRepository;
    private final LangLocalDomainRepository langLocalDomainRepository;
    private final LangLocalRepository langLocalRepository;

    @Override
    @Transactional
    public List<Long> createLangCorpus(CreateLangCorpusRequestDTO langCorpusRequestDTO) {
        List<String> corpusCodes = langCorpusRequestDTO.getCorpusItems().stream()
                .map(LangCorpusItemDTO::getCorpusCode)
                .distinct().toList();
        if (corpusCodes.size() != langCorpusRequestDTO.getCorpusItems().size()) {
            throw new FailedPreconditionException("repeated corpus code");
        }
        List<LangCorpus> existsCorpus = langCorpusDomainRepository.findByApplicationAndCodes(langCorpusRequestDTO.getApplication(), corpusCodes);
        OpLogMDC.putBizCode(String.join(",", corpusCodes));

        List<LangCorpus> toBeSaveLangCorpus = new ArrayList<>(corpusCodes.size());
        // 保存语言定义
        List<CreateLangCorpusCommand> createCommands = langCorpusRequestDTO.getCorpusItems().stream()
                .filter(it -> existsCorpus.stream().noneMatch(old-> old.getCorpusCode().equals(it.getCorpusCode())))
                .map(it -> LangCorpusDTOConverter.INSTANCE.toCreateCommand(langCorpusRequestDTO, it))
                .toList();
        if (!createCommands.isEmpty()) {
            List<LangCorpus> langCorpus = createCommands.stream().map(LangCorpus::create).toList();
            toBeSaveLangCorpus.addAll(langCorpus);
        }
        if (!existsCorpus.isEmpty()) {
            Map<String, UpdateLangCorpusCommand> updateCommands = langCorpusRequestDTO.getCorpusItems().stream()
                    .filter(it -> existsCorpus.stream().anyMatch(old -> old.getCorpusCode().equals(it.getCorpusCode())))
                    .map(it -> Map.entry(it.getCorpusCode(), UpdateLangCorpusCommand.builder()
                            .corpusGroup(langCorpusRequestDTO.getCorpusGroup())
                            .corpusType(langCorpusRequestDTO.getCorpusType())
                            .memo(it.getMemo())
                            .build()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            existsCorpus.forEach(langCorpus -> {
                langCorpus.update(updateCommands.get(langCorpus.getCorpusCode()));
            });
            toBeSaveLangCorpus.addAll(existsCorpus);
        }
        List<LangCorpus> savedLangCorpus = langCorpusDomainRepository.saveAll(toBeSaveLangCorpus);
        // 保存本地化配置
        fillLanguageCorpusLocal(langCorpusRequestDTO.getApplication(), savedLangCorpus, langCorpusRequestDTO.getCorpusItems().stream()
                .collect(Collectors.toMap(LangCorpusItemDTO::getCorpusCode, LangCorpusItemDTO::getLangLocales, (a, b) -> a)));

        return savedLangCorpus.stream().map(IdEntity::getId).toList();
    }

    @Override
    @Transactional
    public void updateLangCorpus(Long langCorpusId, UpdateLangCorpusRequestDTO updateLangCorpusRequestDTO) {
        LangCorpus langCorpus = langCorpusDomainRepository.findById(langCorpusId);
        if (langCorpus == null) {
            throw new NotFoundException("corpus not found");
        }
        OpLogMDC.putBizCode(langCorpus.getCorpusCode());
        UpdateLangCorpusCommand updateLangCorpusCommand = LangCorpusDTOConverter.INSTANCE.toUpdateCommand(updateLangCorpusRequestDTO);
        langCorpus.update(updateLangCorpusCommand);

        langCorpusDomainRepository.save(langCorpus);
    }

    @Override
    @Transactional
    public void deleteLangCorpus(Long langCorpusId) {
        LangCorpus langCorpus = langCorpusDomainRepository.findById(langCorpusId);
        if (langCorpus == null) {
            throw new NotFoundException("lang corpus not exists");
        }
        OpLogMDC.putBizCode(langCorpus.getCorpusCode());
        langCorpusDomainRepository.logicDelete(langCorpusId);

        List<LangLocal> langsLocal = langLocalDomainRepository.findAllByApplicationAndCorpusIds(langCorpus.getApplication(), List.of(langCorpus.getId()));
        langLocalDomainRepository.logicDeleteAll(langsLocal);
    }

    @Override
    public PageDTO<LangCorpusDTO> listLangCorpus(QueryLangCorpusRequestDTO queryDTO) {
        return langCorpusRepository.queryBy(queryDTO);
    }

    @Override
    public void enableLangCorpus(Long langCorpusId, Boolean enabled) {
        LangCorpus langCorpus = langCorpusDomainRepository.findById(langCorpusId);
        if (langCorpus == null) {
            throw new NotFoundException("corpus not found");
        }
        OpLogMDC.putBizCode(langCorpus.getCorpusCode());
        if (enabled) {
            langCorpus.enable();
        } else {
            langCorpus.disable();
        }

        langCorpusDomainRepository.save(langCorpus);

        if (enabled) {
            fillLanguageCorpusLocal(langCorpus.getApplication(), List.of(langCorpus), new HashMap<>());
        }
    }

    @Override
    public LangCorpusDTO getLangCorpus(Long langCorpusId) {
        LangCorpusDTO langCorpus =  langCorpusRepository.queryById(langCorpusId);
        if (langCorpus != null) {
            List<LangLocalDTO> langLocal = langLocalRepository.queryByLangCorpusId(langCorpus.getId());

            langCorpus.setLangLocales(langLocal.stream()
                    .collect(Collectors.toMap(LangLocalDTO::getLangCode,
                            it -> it.getLangValue() == null ? "" : it.getLangValue())));
        }

        return langCorpus;
    }

    @Override
    @Transactional
    public void configLangLocal(Long langCorpusId, Map<String, String> langLocal) {
        LangCorpus langCorpus = langCorpusDomainRepository.findById(langCorpusId);
        if (langCorpus == null) {
            throw new NotFoundException("corpus not found");
        }
        OpLogMDC.putBizCode(langCorpus.getCorpusCode());
        fillLanguageCorpusLocal(langCorpus.getApplication(), List.of(langCorpus), Map.of(langCorpus.getCorpusCode(), langLocal));
    }

    @Override
    public Map<String, String> getLangLocal(String application, String langCode) {
        return langLocalRepository.queryByApplicationAndLangCode(application, langCode);
    }

    protected void fillLanguageCorpusLocal(String application, List<LangCorpus> langCorpus, Map<String, Map<String, String>> localMap) {
        if (CollectionUtils.isEmpty(langCorpus)) {
            return;
        }

        List<Lang> langs = langDomainRepository.findByApplication(application, true);
        if (CollectionUtils.isEmpty(langs)) {
            return;
        }

        List<Long> corpusIds = langCorpus.stream().map(IdEntity::getId).distinct().toList();
        // 已存在的本地化配置
        List<LangLocal> langLocals = langLocalDomainRepository.findAllByApplicationAndCorpusIds(application, corpusIds);

        List<LangLocal> newLangLocals = new LinkedList<>();
        for (LangCorpus corpus: langCorpus) {
            Map<String, LangLocal> existsLangLocalMap = langLocals.stream().filter(it -> Objects.equals(it.getLangCorpusId(), corpus.getId()))
                    .collect(Collectors.toMap(LangLocal::getLangCode, Function.identity(), (a, b) -> a));
            Map<String, String> initLocals = localMap.getOrDefault(corpus.getCorpusCode(), new HashMap<>());
            for (Lang lang : langs) {
                LangLocal existsLangLocal = existsLangLocalMap.get(lang.getCode());
                String local = initLocals.get(lang.getCode());
                if (existsLangLocal == null) {
                    // 不存在，新建默认本地化配置
                    existsLangLocal = LangLocal.createFromCorpus(corpus, lang);
                    if (StringUtils.hasText(local)) {
                        existsLangLocal.updateLocalValue(local);
                    }
                    newLangLocals.add(existsLangLocal);
                } else {
                    // 存在, 如果传入有初始化设置，则更新
                    if (StringUtils.hasText(local)) {
                        existsLangLocal.updateLocalValue(local);
                        newLangLocals.add(existsLangLocal);
                    }
                }
            }
        }

        if (!newLangLocals.isEmpty()) {
            langLocalDomainRepository.saveAll(newLangLocals);
        }
    }
}