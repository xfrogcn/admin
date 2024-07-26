package com.xfrog.platform.application.biz.api;

import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.biz.MaintenanceApi;
import com.xfrog.platform.application.biz.constant.BizOperationLogConstants;
import com.xfrog.platform.application.biz.dto.CacheDTO;
import com.xfrog.platform.application.biz.service.CacheManagerService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MaintenanceController implements MaintenanceApi {

    private final CacheManagerService cacheManagerService;

    @Override
    @Authorization("admin:maintenance:cache")
    public List<CacheDTO> listCaches() {
        return cacheManagerService.listCaches();
    }

    @Override
    @Authorization("admin:maintenance:cache:clear")
    @OperationLog(bizId = "#p0", bizCode = "#p0", bizType = BizOperationLogConstants.BIZ_TYPE_MAINTENANCE, bizAction = BizOperationLogConstants.BIZ_ACTION_CLEAR_CACHE)
    public void clearCaches(String cacheName) {
        cacheManagerService.clearCaches(cacheName);
    }
}
