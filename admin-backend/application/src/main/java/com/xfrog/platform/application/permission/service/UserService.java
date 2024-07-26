package com.xfrog.platform.application.permission.service;


import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.dto.CreateUserRequestDTO;
import com.xfrog.platform.application.permission.dto.CurrentUserInfoDTO;
import com.xfrog.platform.application.permission.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.dto.UpdateUserRequestDTO;
import com.xfrog.platform.application.permission.dto.UserDTO;

import java.util.List;

public interface UserService {

    PageDTO<UserDTO> listUsers(QueryUserRequestDTO queryUserRequestDTO);

    Long createUser(CreateUserRequestDTO user);

    void updateUser(Long userId, UpdateUserRequestDTO user);

    UserDTO getUserDetail(Long userId);

    void resetPassword(Long userId);

    void disableUser(Long userId);

    void enableUser(Long userId);

    void changeCurrentUserPassword(String newPassword);

    void grantRoles(Long userId, List<Long> roleIds);

    CurrentUserInfoDTO getCurrentUserDetail();

    List<String> getCurrentUserPermissionCodes();

    List<String> getUserPermissionCodes(Long userId);
}
