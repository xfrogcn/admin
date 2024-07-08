package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.exception.business.AlreadyExistsException;
import com.xfrog.framework.exception.business.FailedPreconditionException;
import com.xfrog.framework.exception.business.NotFoundException;
import com.xfrog.platform.application.base.dto.CreateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.LangDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryLangCorpusRequestDTO;
import com.xfrog.platform.application.base.dto.UpdateLangCorpusRequestDTO;
import com.xfrog.platform.application.base.repository.LangCorpusRepository;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangFixtures;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LangCorpusServiceImplTest {

    @Mock
    private LangLocalDomainRepository langLocalDomainRepository;

    @Mock
    private LangDomainRepository langDomainRepository;

    @Mock
    private LangCorpusDomainRepository langCorpusDomainRepository;

    @Mock
    private LangCorpusRepository langCorpusRepository;


    @InjectMocks
    private LangCorpusServiceImpl langCorpusService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createLangCorpus_should_throw_exception_when_duplicate_codes() {
        // Given
        CreateLangCorpusRequestDTO requestDTO = LangDTOFixtures.defaultCreateLangCorpusRequestDTO()
                .corpusItems(List.of(LangDTOFixtures.defaultLangCorpusItemDTO().build(),
                        LangDTOFixtures.defaultLangCorpusItemDTO().build()))
                .build();

        // When / Then
        assertThrows(FailedPreconditionException.class, () -> langCorpusService.createLangCorpus(requestDTO));
    }

    @Test
    void createLangCorpus_should_throw_exception_when_corpus_code_exists() {
        // Given
        CreateLangCorpusRequestDTO requestDTO = LangDTOFixtures.defaultCreateLangCorpusRequestDTO()
                .corpusItems(List.of(LangDTOFixtures.defaultLangCorpusItemDTO().corpusCode("test").build()))
                .build();

        doReturn(true)
                .when(langCorpusDomainRepository)
                .existsByApplicationAndCodes(anyString(), anyList());

        // When / Then
        assertThrows(AlreadyExistsException.class, () -> langCorpusService.createLangCorpus(requestDTO));
    }

    @Test
    void createLangCorpus_should_success() {
        // Given
        CreateLangCorpusRequestDTO requestDTO = LangDTOFixtures.defaultCreateLangCorpusRequestDTO()
                .corpusItems(List.of(LangDTOFixtures.defaultLangCorpusItemDTO().corpusCode("test").build()))
                .build();

        doReturn(false)
                .when(langCorpusDomainRepository)
                .existsByApplicationAndCodes(anyString(), anyList());
        Mockito.doAnswer(invocation -> {
                    return invocation.getArgument(0);
                }).when(langCorpusDomainRepository)
                .saveAll(anyList());

        // When
        List<Long> result = langCorpusService.createLangCorpus(requestDTO);

        // Then
        assertThat(result).isNotNull();

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    void updateLangCorpus_WithValidId_ShouldUpdateAndSave() {
        UpdateLangCorpusRequestDTO updateLangCorpusRequestDTO = LangDTOFixtures.defaultUpdateLangCorpusRequestDTO().build();

        doReturn(LangFixtures.createDefaultCorpus().id(1L).build())
                .when(langCorpusDomainRepository)
                .findById(1L);

        langCorpusService.updateLangCorpus(1L, updateLangCorpusRequestDTO);

        verify(langCorpusDomainRepository, times(1)).findById(1L);
        verify(langCorpusDomainRepository, times(1)).save(any(LangCorpus.class));
    }

    @Test
    void updateLangCorpus_WithInvalidId_ShouldThrowNotFoundException() {
        UpdateLangCorpusRequestDTO updateLangCorpusRequestDTO = new UpdateLangCorpusRequestDTO();

        doReturn(null)
                .when(langCorpusDomainRepository)
                .findById(1L);

        assertThrows(NotFoundException.class, () -> {
            langCorpusService.updateLangCorpus(1L, updateLangCorpusRequestDTO);
        });

        verify(langCorpusDomainRepository, times(1)).findById(1L);
        verify(langCorpusDomainRepository, never()).save(any(LangCorpus.class));
    }

    @Test
    void deleteLangCorpus_WithValidId_ShouldDelete() {
        langCorpusService.deleteLangCorpus(1L);
        verify(langCorpusDomainRepository).logicDelete(1L);
    }

    @Test
    void listLangCorpus_Should_Success() {
        langCorpusService.listLangCorpus(new QueryLangCorpusRequestDTO());
        verify(langCorpusRepository).queryAll(any(QueryLangCorpusRequestDTO.class));
    }

    @Test
    void getLangCorpus_Should_Success() {
        langCorpusService.getLangCorpus(1L);
        verify(langCorpusRepository).findById(1L);
    }

    @Test
    void enableLangCorpus_Should_Throw_NotFoundException_When_LangCorpus_NotFound() {

        doReturn(null)
                .when(langCorpusDomainRepository)
                .findById(1L);

        assertThrows(NotFoundException.class, () ->
                langCorpusService.enableLangCorpus(1L, true));
    }

    @Test
    void enableLangCorpus_Should_Enable_LangCorpus_When_Found() {
        doReturn(LangFixtures.createDefaultCorpus().id(1L).build())
                .when(langCorpusDomainRepository)
                .findById(1L);

        langCorpusService.enableLangCorpus(1L, true);

        verify(langCorpusDomainRepository, times(1))
                .save(argThat(LangCorpus::getEnabled));
    }

    @Test
    void enableLangCorpus_Should_Disable_LangCorpus_When_Enabled_Is_False() {
        doReturn(LangFixtures.createDefaultCorpus().id(1L).build())
                .when(langCorpusDomainRepository)
                .findById(1L);

        langCorpusService.enableLangCorpus(1L, false);

        verify(langCorpusDomainRepository, times(1))
                .save(argThat(langCorpus -> !langCorpus.getEnabled()));
    }

    @Nested
    class FillLanguageCorpusLocal {
        @Test
        void should_create_default_localization_when_localization_does_not_exist() {
            Lang lang = LangFixtures.createDefaultLang().build();
            LangCorpus langCorpus = LangFixtures.createDefaultCorpus().build();

            doReturn(List.of(lang,
                    LangFixtures.createDefaultLang()
                            .code("en-US")
                            .name("英语")
                            .localName("English")
                            .id(2L)
                            .build()))
                    .when(langDomainRepository)
                    .findByApplication(anyString(), anyBoolean());
            doReturn(List.of())
                    .when(langLocalDomainRepository)
                    .findAllByApplicationAndCorpusIds(anyString(), anyList());

            langCorpusService.fillLanguageCorpusLocal(LangFixtures.DEFAULT_APPLICATION, List.of(langCorpus), new HashMap<>());

            verify(langLocalDomainRepository, times(1))
                    .saveAll(argThat(langLocals -> langLocals.size() == 2
                            && langLocals.get(0).getLocalValue() == null
                            && !langLocals.get(0).getConfigured()));

        }

        @Test
        void should_create_default_localization_with_initial_values_when_none_exists() {
            Lang lang = LangFixtures.createDefaultLang().build();
            LangCorpus langCorpus = LangFixtures.createDefaultCorpus().build();

            doReturn(List.of(lang))
                    .when(langDomainRepository)
                    .findByApplication(anyString(), anyBoolean());
            doReturn(List.of())
                    .when(langLocalDomainRepository)
                    .findAllByApplicationAndCorpusIds(anyString(), anyList());

            langCorpusService.fillLanguageCorpusLocal(LangFixtures.DEFAULT_APPLICATION, List.of(langCorpus), Map.of(langCorpus.getCorpusCode(), Map.of(lang.getCode(), "TEST")));

            verify(langLocalDomainRepository, times(1))
                    .saveAll(argThat(langLocals -> langLocals.size() == 1
                            && langLocals.get(0).getLocalValue().equals("TEST")
                            && langLocals.get(0).getConfigured()));
        }

        @Test
        void should_not_update_localization_when_exists_and_no_initial_values_provided() {
            Lang lang = LangFixtures.createDefaultLang().build();
            LangCorpus langCorpus = LangFixtures.createDefaultCorpus().build();

            doReturn(List.of(lang))
                    .when(langDomainRepository)
                    .findByApplication(anyString(), anyBoolean());
            doReturn(List.of(LangFixtures.createDefaultLocal(langCorpus, lang).build()))
                    .when(langLocalDomainRepository)
                    .findAllByApplicationAndCorpusIds(anyString(), anyList());

            langCorpusService.fillLanguageCorpusLocal(LangFixtures.DEFAULT_APPLICATION, List.of(langCorpus), new HashMap<>());

            verify(langLocalDomainRepository, times(0))
                    .saveAll(anyList());
        }

        @Test
        void should_update_localization_when_exists_and_initial_values_provided() {
            Lang lang = LangFixtures.createDefaultLang().build();
            LangCorpus langCorpus = LangFixtures.createDefaultCorpus().build();

            doReturn(List.of(lang))
                    .when(langDomainRepository)
                    .findByApplication(anyString(), anyBoolean());
            doReturn(List.of(LangFixtures.createDefaultLocal(langCorpus, lang).build()))
                    .when(langLocalDomainRepository)
                    .findAllByApplicationAndCorpusIds(anyString(), anyList());

            langCorpusService.fillLanguageCorpusLocal(LangFixtures.DEFAULT_APPLICATION, List.of(langCorpus), Map.of(langCorpus.getCorpusCode(), Map.of(lang.getCode(), "TEST")));

            verify(langLocalDomainRepository, times(1))
                    .saveAll(argThat(langLocals -> langLocals.size() == 1
                            && langLocals.get(0).getLocalValue().equals("TEST")
                            && langLocals.get(0).getConfigured()));
        }
    }

}