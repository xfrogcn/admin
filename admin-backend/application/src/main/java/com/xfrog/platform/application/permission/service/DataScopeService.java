package com.xfrog.platform.application.permission.service;

import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.api.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;

import java.util.List;

public interface DataScopeService {
    void grantDataScope(GrantDataScopeRequestDTO requestDTO);

    List<DataScopeDTO> getDataScopes(DataScopeTargetType targetType, Long targetId);

    List<DataScopeDTO> getUserDataScopes(Long userId);
}
