package com.xfrog.platform.application.biz;

import com.xfrog.platform.application.biz.dto.CacheDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "MaintenanceApi", description = "运维接口")
@RequestMapping("/api/maintenance")
public interface MaintenanceApi {
    @GetMapping("/caches")
    @Operation(description = "获取缓存列表")
    List<CacheDTO> listCaches();

    @DeleteMapping("/caches/clear/{cacheName}")
    @Operation(description = "清空缓存")
    void clearCaches(@PathVariable(name = "cacheName") String cacheName);
}
