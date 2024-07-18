package com.xfrog.platform.application.permission.repository;


import com.xfrog.framework.repository.CacheablePageableApplicationRepository;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserRepository extends CacheablePageableApplicationRepository<UserDTO, QueryUserRequestDTO> {
    List<String> queryUserPermissionCodes(Long userId);

    void removeUserPermissionCodesCache(Long userId);

    List<Long> queryUserRoleIds(Long userId);

    void removeUserRoleIdsCache(Long userId);

    Map<Long, List<Long>> queryUserRoleIds(List<Long> userIds);
}
