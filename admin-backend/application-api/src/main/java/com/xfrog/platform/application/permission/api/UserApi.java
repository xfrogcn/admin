package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CurrentUserInfoDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Tag(name = "UserApi", description = "用户管理接口")
@RequestMapping("/api/users")
public interface UserApi {
    @PostMapping("/list")
    @Operation(summary = "查询用户列表")
    PageDTO<UserDTO> listUsers(@Valid @RequestBody QueryUserRequestDTO queryUserRequestDTO);

    @PostMapping
    @Operation(summary = "创建用户")
    Long createUser(@Valid @RequestBody CreateUserRequestDTO user);

    @PutMapping("/{userId}")
    @Operation(summary = "更新用户")
    void updateUser(@PathVariable(name = "userId") Long userId, @Valid @RequestBody UpdateUserRequestDTO user);

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情")
    UserDTO getUserDetail(@PathVariable(name = "userId") Long userId);

    @PutMapping("/reset-password/{userId}")
    @Operation(summary = "重置用户密码")
    void resetPassword(@PathVariable(name = "userId") Long userId);

    @PutMapping("/disable/{userId}")
    @Operation(summary = "禁用用户")
    void disableUser(@PathVariable(name = "userId") Long userId);

    @PutMapping("/enable/{userId}")
    @Operation(summary = "启用用户")
    void enableUser(@PathVariable(name = "userId") Long userId);

    @PutMapping("/change-password")
    @Operation(summary = "修改当前用户密码")
    void changeCurrentUserPassword(@RequestParam String newPassword);

    @PutMapping("/grant-roles/{userId}")
    @Operation(summary = "为用户授予角色")
    void grantRoles(@PathVariable(name = "userId") Long userId, @RequestBody List<Long> roleIds);

    @GetMapping("/current")
    @Operation(summary = "获取当前用户详情")
    CurrentUserInfoDTO getCurrentUserDetail();

    @GetMapping("/current/permission-codes")
    @Operation(summary = "获取当前用户权限列表")
    List<String> getCurrentUserPermissionCodes();
}
