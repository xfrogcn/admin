package com.xfrog.platform.application.permission.api;

import com.xfrog.platform.application.permission.api.dto.CreateRoleRequestDTO;
import com.xfrog.platform.application.permission.api.dto.PermissionItemDTO;
import com.xfrog.platform.application.permission.api.dto.RoleDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateRoleRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "RoleApi", description = "角色管理接口")
@RequestMapping("/api/roles")
public interface RoleApi {
    @PostMapping
    @Operation(summary = "创建角色")
    Long createRole(@Valid @RequestBody CreateRoleRequestDTO createRoleRequestDTO);

    @GetMapping("/list")
    @Operation(summary = "查询角色列表")
    List<RoleDTO> listRoles();

    @GetMapping("/{roleId}/permission-items")
    @Operation(summary = "获取角色权限列表")
    List<PermissionItemDTO> getRolePermissionItems(@PathVariable("roleId") Long roleId);

    @PostMapping("/{roleId}")
    @Operation(summary = "更新角色")
    void updateRole(@PathVariable("roleId") Long roleId, @Valid @RequestBody UpdateRoleRequestDTO updateRoleRequestDTO);

    @DeleteMapping("/{roleId}")
    @Operation(summary = "删除角色")
    void deleteRole(@PathVariable("roleId") Long roleId);

    @PostMapping("/{roleId}/{enabled}")
    @Operation(summary = "启用或禁用角色")
    void enableRole(@PathVariable("roleId") Long roleId, @PathVariable("enabled") Boolean enabled);

    @PutMapping("/grant-permissions/{roleId}")
    @Operation(summary = "为角色授予权限")
    void grantPermissionItems(@PathVariable(name = "roleId") Long roleId, @RequestBody List<Long> permissionItemIds);
}
