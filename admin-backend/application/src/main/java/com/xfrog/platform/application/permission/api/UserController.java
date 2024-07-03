package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CurrentUserInfoDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.application.permission.service.UserService;
import com.xfrog.platform.application.resourceserver.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Authorization(value = "admin:system:user", demoDisabled = false)
    @Override
    public PageDTO<UserDTO> listUsers(QueryUserRequestDTO queryUserRequestDTO) {
        return userService.listUsers(queryUserRequestDTO);
    }

    @Authorization("admin:system:user:create")
    @Override
    public Long createUser(CreateUserRequestDTO user) {
        return userService.createUser(user);
    }

    @Authorization("admin:system:user:edit")
    @Override
    public void updateUser(Long userId, UpdateUserRequestDTO user) {
        userService.updateUser(userId, user);
    }

    @Authorization(value = "admin:system:user", demoDisabled = false)
    @Override
    public UserDTO getUserDetail(Long userId) {
        return userService.getUserDetail(userId);
    }

    @Authorization("admin:system:user:resetpassword")
    @Override
    public void resetPassword(Long userId) {
        userService.resetPassword(userId);
    }

    @Authorization("admin:system:user:disable")
    @Override
    public void disableUser(Long userId) {
        userService.disableUser(userId);
    }

    @Authorization("admin:system:user:disable")
    @Override
    public void enableUser(Long userId) {
        userService.enableUser(userId);
    }

    @Override
    public void changeCurrentUserPassword(String newPassword) {
        userService.changeCurrentUserPassword(newPassword);
    }

    @Authorization("admin:system:user:grantrole")
    @Override
    public void grantRoles(Long userId, List<Long> roleIds) {
        userService.grantRoles(userId, roleIds);
    }

    @Override
    public CurrentUserInfoDTO getCurrentUserDetail() {
        return userService.getCurrentUserDetail();
    }

    @Override
    public List<String> getCurrentUserPermissionCodes() {
        return userService.getCurrentUserPermissionCodes();
    }
}
