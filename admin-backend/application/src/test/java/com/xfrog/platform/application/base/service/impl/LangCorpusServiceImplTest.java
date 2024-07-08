package com.xfrog.platform.application.base.service.impl;

import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangFixtures;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LangCorpusServiceImplTest {

    @Mock
    private LangLocalDomainRepository langLocalDomainRepository;

    @Mock
    private LangDomainRepository langDomainRepository;

    @InjectMocks
    private LangCorpusServiceImpl langCorpusService;
    @BeforeEach
    void setUp() {
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
        void  should_create_default_localization_with_initial_values_when_none_exists() {
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
        void  should_not_update_localization_when_exists_and_no_initial_values_provided() {
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
        void  should_update_localization_when_exists_and_initial_values_provided() {
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