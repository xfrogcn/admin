package com.xfrog.platform.application.permission.api;

import com.xfrog.framework.dto.PageDTO;
import com.xfrog.framework.oplog.OperationActionConstants;
import com.xfrog.framework.oplog.annotation.OperationLog;
import com.xfrog.platform.application.permission.api.constant.PermissionOperationLogConstants;
import com.xfrog.platform.application.permission.api.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.CurrentUserInfoDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.application.permission.service.UserService;
import com.xfrog.platform.application.resourceserver.Authorization;
import com.xfrog.platform.application.resourceserver.IgnoreDataPermission;
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
    @OperationLog(bizId = "#return", bizCode = "#p0.userName", bizType = PermissionOperationLogConstants.BIZ_TYPE_USER, bizAction = OperationActionConstants.CREATE)
    public Long createUser(CreateUserRequestDTO user) {
        return userService.createUser(user);
    }

    @Authorization("admin:system:user:edit")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_USER, bizAction = OperationActionConstants.UPDATE)
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
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_USER, bizAction = OperationActionConstants.PASSWORD_RESET)
    public void resetPassword(Long userId) {
        userService.resetPassword(userId);
    }

    @Authorization("admin:system:user:disable")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_USER, bizAction = OperationActionConstants.DISABLE)
    public void disableUser(Long userId) {
        userService.disableUser(userId);
    }

    @Authorization("admin:system:user:disable")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_USER, bizAction = OperationActionConstants.ENABLE)
    public void enableUser(Long userId) {
        userService.enableUser(userId);
    }

    @Override
    @OperationLog(bizId = "#userId", bizType = PermissionOperationLogConstants.BIZ_TYPE_USER, bizAction = OperationActionConstants.PASSWORD_CHANGE)
    public void changeCurrentUserPassword(String newPassword) {
        userService.changeCurrentUserPassword(newPassword);
    }

    @Authorization("admin:system:user:grantrole")
    @Override
    @OperationLog(bizId = "#p0", bizType = PermissionOperationLogConstants.BIZ_TYPE_USER, bizAction = OperationActionConstants.PERMISSION_CHANGE, extra = "json(#p1)")
    public void grantRoles(Long userId, List<Long> roleIds) {
        userService.grantRoles(userId, roleIds);
    }

    @Override
    @IgnoreDataPermission
    public CurrentUserInfoDTO getCurrentUserDetail() {
        return userService.getCurrentUserDetail();
    }

    @Override
    @IgnoreDataPermission
    public List<String> getCurrentUserPermissionCodes() {
        return userService.getCurrentUserPermissionCodes();
    }
}
