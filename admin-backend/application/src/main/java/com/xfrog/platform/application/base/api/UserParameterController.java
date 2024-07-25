package com.xfrog.platform.application.base.api;

import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.base.UserParameterApi;
import com.xfrog.platform.application.base.constant.BaseOperationLogConstants;
import com.xfrog.platform.application.base.dto.UpdateUserParameterRequestDTO;
import com.xfrog.platform.application.base.dto.UserSettingsDTO;
import com.xfrog.platform.application.base.service.UserParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserParameterController implements UserParameterApi {
    private final UserParameterService userParameterService;


    @Override
    public UserSettingsDTO getUserSettings(String application) {
        return userParameterService.getUserSettings(application);
    }

    @Override
    @OperationLog(bizId = "#userId", bizType = BaseOperationLogConstants.BIZ_TYPE_USER_PARAMETER, bizAction = OperationActionConstants.UPDATE)
    public void updateUserParameters(String application, UpdateUserParameterRequestDTO requestDTO) {
        userParameterService.updateUserParameters(application, requestDTO);
    }
}
