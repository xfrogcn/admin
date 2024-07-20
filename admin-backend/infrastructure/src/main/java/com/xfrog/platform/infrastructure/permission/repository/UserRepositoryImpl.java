package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.permission.api.dto.QueryUserRequestDTO;
import com.xfrog.platform.application.permission.api.dto.UserDTO;
import com.xfrog.platform.application.permission.repository.UserRepository;
import com.xfrog.platform.infrastructure.permission.common.PermissionCacheNames;
import com.xfrog.platform.infrastructure.permission.converter.UserPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import com.xfrog.platform.infrastructure.permission.dataobject.UserRolePO;
import com.xfrog.platform.infrastructure.permission.mapper.UserMapper;
import com.xfrog.platform.infrastructure.permission.mapper.UserRoleMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseCacheablePageableApplicationRepository;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl extends BaseCacheablePageableApplicationRepository<UserDTO, UserPO, UserMapper, QueryUserRequestDTO>
        implements UserRepository {

    private final UserRoleMapper userRoleMapper;

    public UserRepositoryImpl(UserMapper mapper, UserRoleMapper userRoleMapper) {
        super(mapper, UserPOConverter.INSTANCE);
        this.userRoleMapper = userRoleMapper;
    }

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "u.created_time",
                    "lastLoginTime", "u.last_login_time",
                    "name", "u.name",
                    "userName", "u.user_name",
                    "organizationName", "org.name"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }

    @Override
    public String getCacheName() {
        return PermissionCacheNames.USER_DETAIL;
    }

    @Override
    @Cacheable(cacheNames = PermissionCacheNames.USER_PERMISSION_CODES, key = "#p0")
    public List<String> queryUserPermissionCodes(Long userId) {
        return mapper.queryUserPermissionCodes(userId);
    }

    @Override
    @CacheEvict(cacheNames = PermissionCacheNames.USER_PERMISSION_CODES, key = "#p0")
    public void removeUserPermissionCodesCache(Long userId) {
        // nothing
    }

    @Override
    @Cacheable(cacheNames = PermissionCacheNames.USER_ROLE_IDS, key = "#p0")
    public List<Long> queryUserRoleIds(Long userId) {
        LambdaQueryWrapper<UserRolePO> queryWrapper = new LambdaQueryWrapper<UserRolePO>()
                .eq(UserRolePO::getDeleted, false)
                .eq(UserRolePO::getUserId, userId)
                .select(UserRolePO::getRoleId);
        return userRoleMapper.selectObjs(queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = PermissionCacheNames.USER_ROLE_IDS, key = "#p0")
    public void removeUserRoleIdsCache(Long userId) {
        // nothing
    }

    @Override
    public Map<Long, List<Long>> queryUserRoleIds(List<Long> userIds) {
        return batchKeysCache.runWithBatchKeyCache(PermissionCacheNames.USER_ROLE_IDS, (keys) -> {
            LambdaQueryWrapper<UserRolePO> queryWrapper = new LambdaQueryWrapper<UserRolePO>()
                    .eq(UserRolePO::getDeleted, false)
                    .in(UserRolePO::getUserId, userIds);
            List<UserRolePO> userRolePOS = userRoleMapper.selectList(queryWrapper);

            return userRolePOS.stream()
                    .collect(Collectors.groupingBy(UserRolePO::getUserId,
                            Collectors.mapping(UserRolePO::getRoleId, Collectors.toList())));
        }, userIds);
    }
}
