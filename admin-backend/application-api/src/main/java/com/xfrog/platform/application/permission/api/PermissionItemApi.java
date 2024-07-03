package com.xfrog.platform.application.permission.api;

import com.xfrog.platform.application.permission.api.dto.CreatePermissionItemRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.UpdatePermissionItemRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "PermissionItemApi", description = "权限项管理接口")
@RequestMapping("/api/permission-items")
public interface PermissionItemApi {
    @PostMapping
    @Operation(summary = "创建权限项")
    Long createPermissionItem(@Valid @RequestBody CreatePermissionItemRequestDTO permissionItemRequestDTO);

    @PostMapping("/{permissionItemId}")
    @Operation(summary = "更新权限项")
    void updatePermissionItem(@PathVariable(name = "permissionItemId") Long permissionItemId, @Valid @RequestBody UpdatePermissionItemRequestDTO permissionItem);

    @GetMapping("/list")
    @Operation(summary = "获取权限项列表")
    List<PermissionItemDTO> listPermissionItems();

    @GetMapping("/list/platform")
    @Operation(summary = "获取权限项列表（包含平台权限项）")
    List<PermissionItemDTO> listPermissionItemsFormPlatform();

    @DeleteMapping("/{permissionItemId}")
    @Operation(summary = "删除权限项")
    void deletePermissionItem(@PathVariable(name = "permissionItemId") Long permissionItemId);
}
