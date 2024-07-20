package com.xfrog.platform.application.permission.api;

import com.xfrog.platform.application.permission.api.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.api.dto.GrantDataScopeRequestDTO;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "DataScopeApi", description = "数据权限接口")
@RequestMapping("/api/data-scopes")
public interface DataScopeApi {
    @PutMapping("/grant")
    @Operation(summary = "数据赋权")
    void grantDataScope(@Valid @RequestBody GrantDataScopeRequestDTO requestDTO);

    @GetMapping("/{targetType}/{targetId}")
    @Operation(summary = "获取数据权限")
    List<DataScopeDTO> getDataScopes(@PathVariable("targetType") @Valid @NotNull DataScopeTargetType targetType, @PathVariable("targetId") Long targetId);

    @GetMapping("/users/{userId}")
    @Operation(summary = "获取用户数据权限")
    List<DataScopeDTO> getUserDataScopes(@PathVariable("userId") Long userId);
}
