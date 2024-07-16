package com.xfrog.platform.application.base.service.impl;

import com.xfrog.framework.principal.CurrentPrincipalContext;
import com.xfrog.framework.principal.PrincipalInfo;
import com.xfrog.platform.application.base.dto.LangDTOFixtures;
import com.xfrog.platform.application.base.dto.QueryLangRequestDTO;
import com.xfrog.platform.application.base.dto.UserSettingsDTO;
import com.xfrog.platform.application.base.repository.LangRepository;
import com.xfrog.platform.application.base.repository.UserParameterRepository;
import com.xfrog.platform.domain.base.aggregate.LangFixtures;
import com.xfrog.platform.domain.base.repository.UserParameterDomainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserParameterServiceImplTest {
    @Mock
    private UserParameterRepository userParameterRepository;
    @Mock
    private UserParameterDomainRepository userParameterDomainRepository;
    @Mock
    private LangRepository langRepository;

    @InjectMocks
    private UserParameterServiceImpl userParameterService;

    @Test
    void getUserSettings_ShouldReturnEmptySettingsWhenNotHasCurrentUser() {

        UserSettingsDTO userSettings = userParameterService.getUserSettings();

        assertThat(userSettings).isNotNull();
        assertThat(userSettings.getLangs()).isEmpty();
        assertThat(userSettings.getParameters()).isEmpty();
    }

    @Test
    void getUserSettings_ShouldReturnLangsAndUserParameters() {
        String application = LangFixtures.DEFAULT_APPLICATION;
        CurrentPrincipalContext.setCurrentPrincipal(PrincipalInfo.create(1L, "admin", 1L, "admin", "admin"));

        when(langRepository.queryAllByApplication(application, true))
                .thenReturn(List.of(
                        LangDTOFixtures.defaultLangDTO().code("zh-CN").build()
                ));
        when(userParameterRepository.queryUserParameters(1L))
                .thenReturn(Map.of("lang", "zh-CN"));

        UserSettingsDTO userSettings = userParameterService.getUserSettings();
        assertThat(userSettings).isNotNull();
        assertThat(userSettings.getLangs()).hasSize(1);
        assertThat(userSettings.getLangs().get(0).getCode()).isEqualTo("zh-CN");
        assertThat(userSettings.getParameters()).isNotEmpty();
        assertThat(userSettings.getParameters().get("lang")).isEqualTo("zh-CN");
    }

    @Test
    void updateUserParameters() {
    }
}