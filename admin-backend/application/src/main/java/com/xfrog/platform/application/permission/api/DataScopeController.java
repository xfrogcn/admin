package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.permission.api.constant.PermissionOperationLogConstants;
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
    @OperationLog(bizId = "format('%s-%s', #p0.targetType, #p0.targetId)",
            bizCode = "format('%s-%s', #p0.targetType, #p0.targetId)",
            bizType = PermissionOperationLogConstants.BIZ_TYPE_DATA_SCOPE,
            bizAction = OperationActionConstants.UPDATE,
            extra = "json(#p0)")
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
