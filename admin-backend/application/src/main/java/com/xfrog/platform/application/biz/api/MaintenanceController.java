package com.xfrog.platform.application.biz.api;

import com.xfrog.platform.application.biz.MaintenanceApi;
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
    @Authorization("admin:platform:cache")
    public List<CacheDTO> listCaches() {
        return cacheManagerService.listCaches();
    }

    @Override
    @Authorization("admin:platform:cache:clear")
    public void clearCaches(String cacheName) {
        cacheManagerService.clearCaches(cacheName);
    }
}
