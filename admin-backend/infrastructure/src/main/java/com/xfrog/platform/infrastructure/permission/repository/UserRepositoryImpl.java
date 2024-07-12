package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xfrog.framework.dto.PageDTO;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.infrastructure.permission.converter.UserPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import com.xfrog.platform.infrastructure.permission.mapper.UserMapper;
import com.xfrog.platform.infrastructure.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;
    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "u.created_time",
                    "lastLoginTime", "u.last_login_time",
                    "name", "u.name",
                    "userName", "u.user_name",
                    "organizationName", "org.name"));

    @Override
    public PageDTO<UserDTO> queryAllBy(QueryUserRequestDTO queryDTO) {
        Page<UserDTO> page = PageUtils.page(queryDTO, ORDER_FIELD_MAP);
        List<UserDTO> userDTOS = userMapper.queryAllBy(queryDTO, page);
        return PageUtils.result(page, userDTOS);
    }

    @Override
    public UserDTO queryById(Long userId) {
        LambdaQueryWrapper<UserPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPO::getDeleted, false);
        queryWrapper.eq(UserPO::getId, userId);
        UserPO userPO =  userMapper.selectOne(queryWrapper);

        return UserPOConverter.INSTANCE.toDTO(userPO);
    }

    @Override
    public List<String> queryUserPermissionCodes(Long userId) {
        return userMapper.queryUserPermissionCodes(userId);
    }
}
