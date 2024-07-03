package com.xfrog.platform.infrastructure.permission.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapperEx<UserPO> {
    List<UserDTO> queryAllBy(@Param("queryDTO") QueryUserRequestDTO queryDTO, @Param("page") Page<UserDTO> page);

    void updateLastLoginTime(@Param("userId") Long userId, @Param("lastLoginTime") LocalDateTime lastLoginTime);

    List<String> queryUserPermissionCodes(@Param("userId") Long userId);
}
