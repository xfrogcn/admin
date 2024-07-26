package com.xfrog.platform.infrastructure.permission.mapper;

import com.xfrog.platform.application.permission.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.dto.UserDTO;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper extends PageableMapper<UserPO, UserDTO, QueryUserRequestDTO> {
    void updateLastLoginTime(@Param("userId") Long userId, @Param("lastLoginTime") LocalDateTime lastLoginTime);

    List<String> queryUserPermissionCodes(@Param("userId") Long userId);
}
