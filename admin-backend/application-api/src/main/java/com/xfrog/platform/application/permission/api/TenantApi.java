package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.api.dto.CreateTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.QueryTenantRequestDTO;
import com.xfrog.platform.application.permission.api.dto.TenantDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateTenantRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "TenantApi", description = "租户管理接口")
@RequestMapping("/api/tenants")
public interface TenantApi {
    @PostMapping
    @Operation(description = "创建租户")
    Long createTenant(@Valid @RequestBody CreateTenantRequestDTO tenantDTO);

    @PostMapping("/list")
    @Operation(summary = "查询租户列表")
    PageDTO<TenantDTO> listTenants(@RequestBody QueryTenantRequestDTO requestDTO);

    @PutMapping("/{tenantId}")
    @Operation(summary = "更新租户")
    void updateTenant(@PathVariable("tenantId") Long tenantId, @Valid @RequestBody UpdateTenantRequestDTO tenantDTO);

    @PostMapping("/{tenantId}/{enabled}")
    @Operation(summary = "启用或禁用租户")
    void enableTenant(@PathVariable("tenantId") Long tenantId, @PathVariable("enabled") Boolean enabled);

}
