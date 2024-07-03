package com.xfrog.platform.application.permission.api;

import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.api.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.application.permission.service.DataScopeService;
import com.xfrog.platform.application.resourceserver.Authorization;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataScopeController implements DataScopeApi {

    private final DataScopeService dataScopeService;

    @Override
    @Authorization("admin:system:user:grantdatascope|admin:system:role:grantdatascope")
    public void grantDataScope(GrantDataScopeRequestDTO requestDTO) {
        dataScopeService.grantDataScope(requestDTO);
    }

    @Override
    @Authorization("admin:system:user:grantdatascope|admin:system:role:grantdatascope")
    public List<DataScopeDTO> getDataScopes(DataScopeTargetType targetType, Long targetId) {
        return dataScopeService.getDataScopes(targetType, targetId);
    }

    @Override
    @Authorization("admin:system:user:datascope")
    public List<DataScopeDTO> getUserDataScopes(Long userId) {
        return dataScopeService.getUserDataScopes(userId);
    }
}
