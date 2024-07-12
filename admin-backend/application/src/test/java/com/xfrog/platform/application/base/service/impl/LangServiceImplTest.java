package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.base.dto.CreateLangRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangRequestDTO;
import com.xfrog.platform.application.base.repository.LangCorpusRepository;
import com.xfrog.platform.application.base.repository.LangRepository;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangFixtures;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LangServiceImplTest {

    @Mock
    private LangLocalDomainRepository langLocalDomainRepository;

    @Mock
    private LangDomainRepository langDomainRepository;

    @Mock
    private LangCorpusDomainRepository langCorpusDomainRepository;

    @Mock
    private LangCorpusRepository langCorpusRepository;

    @Mock
    private LangRepository langRepository;


    @InjectMocks
    private LangServiceImpl langService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createLanguage_shouldThrowFailedPreconditionExceptionWhenLanguageAlreadyExists() {
        when(langDomainRepository.existsByCode(LangFixtures.DEFAULT_APPLICATION, "zh-CN"))
                .thenReturn(true);

        CreateLangRequestDTO createLangRequestDTO = LangDTOFixtures.defaultCreateLangRequestDTO().build();

        assertThrows(FailedPreconditionException.class, () -> langService.createLanguage(createLangRequestDTO));

        verify(langDomainRepository, times(0)).save(any(Lang.class));

    }

    @Test
    void createLanguage_shouldCreateLanguageSuccessfullyWhenItDoesNotExist() {
        when(langDomainRepository.existsByCode(LangFixtures.DEFAULT_APPLICATION, "zh-CN"))
                .thenReturn(false);
        when(langDomainRepository.save(any(Lang.class))).thenAnswer(invocation -> LangFixtures.createDefaultLang().build());

        CreateLangRequestDTO createLangRequestDTO = LangDTOFixtures.defaultCreateLangRequestDTO().build();

        Long createdLanguageId = langService.createLanguage(createLangRequestDTO);

        assertThat(createdLanguageId).isNotNull();
        verify(langDomainRepository, times(1)).save(any(Lang.class));
    }

    @Test
    void updateLanguage_updateLanguageShouldUpdateWhenFound() {
        Lang lang = LangFixtures.createDefaultLang().build();
        when(langDomainRepository.findById(lang.getId())).thenReturn(lang);

        UpdateLangRequestDTO updateLangRequestDTO = LangDTOFixtures.defaultUpdateLangRequestDTO()
                .name("china").localName("简体中文")
                .build();
        langService.updateLanguage(lang.getId(), updateLangRequestDTO);

        verify(langDomainRepository, times(1))
                .save(argThat(domain -> domain.getId().equals(lang.getId())
                        && domain.getName().equals(updateLangRequestDTO.getName())
                        && domain.getLocalName().equals(updateLangRequestDTO.getLocalName())
                ));
    }

    @Test
    void updateLanguage_updateLanguageShouldThrowNotFoundExceptionWhenNotFound() {
        when(langDomainRepository.findById(1L)).thenReturn(null);

        UpdateLangRequestDTO updateLangRequestDTO = LangDTOFixtures.defaultUpdateLangRequestDTO().build();
        assertThrows(NotFoundException.class, () -> langService.updateLanguage(1L, updateLangRequestDTO));

        verify(langDomainRepository, never()).save(any(Lang.class));
    }


    @Test
    void deleteLanguage_ShouldSuccessfully() {
        langService.deleteLanguage(1L);
        verify(langDomainRepository, times(1)).logicDelete(1L);
    }

    @Test
    void listLanguages_ShouldSuccessfully() {
        langService.listLanguages(QueryLangRequestDTO.builder().pageNum(1).pageSize(10).build());
        verify(langRepository,times(1)).queryBy(any(QueryLangRequestDTO.class));
    }

    @Test
    void getLanguage_ShouldSuccessfully() {
        langService.getLanguage(1L);
        verify(langRepository, times(1)).queryById(1L);
    }

    @Test
    void enableLanguage_ShouldThrowNotFoundExceptionWhenLangNotFound() {
        when(langDomainRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                langService.enableLanguage(1L, true, null));
    }

    @Test
    void enableLanguage_ShouldEnableLangWhenEnabledIsTrue() {
        Lang lang = LangFixtures.createDefaultLang().build();

        when(langDomainRepository.findById(lang.getId())).thenReturn(lang);

        langService.enableLanguage(lang.getId(), true, null);

        verify(langDomainRepository, times(1))
                .save(argThat(Lang::getEnabled));
    }

    @Test
    void enableLanguage_ShouldDisableLangWhenEnabledIsFalse() {
        Lang lang = LangFixtures.createDefaultLang().build();

        when(langDomainRepository.findById(lang.getId())).thenReturn(lang);

        langService.enableLanguage(lang.getId(), false, null);

        verify(langDomainRepository, times(1))
                .save(argThat(domain -> !domain.getEnabled()));
    }

    @Test
    void fillLanguageCorpusLocal_shouldAutoCompleteLangLocalConfigWhenNoLocalizationExists() {
        Lang zhLang = LangFixtures.createDefaultLang().code("zh-CN").build();
        Lang enLang = LangFixtures.createDefaultLang().code("en-US").build();
        LangCorpus langCorpus1 = LangFixtures.createDefaultCorpus().corpusCode("code1").build();
        LangCorpus langCorpus2 = LangFixtures.createDefaultCorpus().corpusCode("code2").build();
        LangLocal zhLangLocalCode1 = LangFixtures.createDefaultLocal(langCorpus1, zhLang).build();
        LangLocal zhLangLocalCode2 = LangFixtures.createDefaultLocal(langCorpus2, zhLang).build();
        LangLocal enLangLocalCode1 = LangFixtures.createDefaultLocal(langCorpus1, enLang).build();

        doReturn(List.of(zhLang, enLang))
                .when(langDomainRepository)
                        .findByIds(anyList());
        doReturn(List.of(langCorpus1, langCorpus2))
                .when(langCorpusDomainRepository)
                        .findAllByApplication(anyString());
        doReturn(List.of(zhLangLocalCode1, zhLangLocalCode2, enLangLocalCode1))
                .when(langLocalDomainRepository)
                        .findAllByApplicationAndLangId(anyString(), anyList());

        langService.fillLanguageCorpusLocal(LangFixtures.DEFAULT_APPLICATION, enLang.getId(), zhLang.getId());

        verify(langLocalDomainRepository, times(1))
                .saveAll(argThat(langLocals -> langLocals.size() == 1
                        && !langLocals.get(0).getConfigured()
                        && langLocals.get(0).getLangCorpusCode().equals("code2")));
    }


}