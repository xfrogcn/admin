package com.xfrog.platform.application.base.service.impl;

import com.xfrog.platform.application.base.dto.UpdateUserParameterRequestDTO;
import com.xfrog.platform.application.base.dto.UserSettingsDTO;
import com.xfrog.platform.application.base.repository.UserParameterRepository;
import com.xfrog.platform.application.base.service.UserParameterService;
import com.xfrog.platform.domain.base.repository.UserParameterDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserParameterServiceImpl implements UserParameterService {

    private UserParameterRepository userParameterRepository;
    private UserParameterDomainRepository userParameterDomainRepository;

    @Override
    public UserSettingsDTO getUserSettings() {
        return null;
    }

    @Override
    public void updateUserParameters(UpdateUserParameterRequestDTO requestDTO) {

    }
}
