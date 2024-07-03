package com.xfrog.platform.application.permission.repository;


import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;

import java.util.List;

public interface UserRepository {
    PageDTO<UserDTO> queryAllBy(QueryUserRequestDTO queryDTO);

    List<String> queryUserPermissionCodes(Long userId);
}
