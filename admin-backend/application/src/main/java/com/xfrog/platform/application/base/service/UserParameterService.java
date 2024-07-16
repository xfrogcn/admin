package com.xfrog.platform.application.base.service;

import com.xfrog.platform.application.base.dto.UpdateUserParameterRequestDTO;
import com.xfrog.platform.application.base.dto.UserSettingsDTO;

public interface UserParameterService {

    UserSettingsDTO getUserSettings();

    void updateUserParameters(UpdateUserParameterRequestDTO requestDTO);
}
