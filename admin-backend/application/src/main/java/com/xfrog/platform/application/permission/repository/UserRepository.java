package com.xfrog.platform.application.permission.repository;


import com.xfrog.framework.repository.PageableApplicationRepository;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;

import java.util.List;

public interface UserRepository extends PageableApplicationRepository<UserDTO, QueryUserRequestDTO> {
    List<String> queryUserPermissionCodes(Long userId);
}
