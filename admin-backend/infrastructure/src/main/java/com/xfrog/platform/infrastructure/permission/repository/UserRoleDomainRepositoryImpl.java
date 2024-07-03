package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.UserRole;
import com.xfrog.platform.domain.permission.repository.UserRoleDomainRepository;
import com.xfrog.platform.infrastructure.permission.converter.UserRolePOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.UserRolePO;
import com.xfrog.platform.infrastructure.permission.mapper.UserRoleMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRoleDomainRepositoryImpl extends BaseDomainRepository<UserRole, UserRolePO, UserRoleMapper> implements UserRoleDomainRepository {
    public UserRoleDomainRepositoryImpl(UserRoleMapper userRoleMapper) {
        converter = UserRolePOConverter.INSTANCE;
        mapper = userRoleMapper;
    }

    @Override
    public boolean existsByRoleId(Long roleId) {
        LambdaQueryWrapper<UserRolePO> queryWrapper = new LambdaQueryWrapper<UserRolePO>()
                .eq(UserRolePO::getDeleted, false)
                .eq(UserRolePO::getRoleId, roleId);
        return mapper.exists(queryWrapper);
    }

    @Override
    public List<UserRole> getByUserId(Long userId) {
        List<UserRolePO> pos = mapper.selectList(new LambdaQueryWrapper<UserRolePO>()
                .eq(UserRolePO::getDeleted, false)
                .eq(UserRolePO::getUserId, userId));
        return converter.toDomainList(pos);
    }

    @Override
    public List<UserRole> getByUserIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        List<UserRolePO> pos = mapper.selectList(new LambdaQueryWrapper<UserRolePO>()
                .eq(UserRolePO::getDeleted, false)
                .in(UserRolePO::getUserId, userIds));
        return converter.toDomainList(pos);
    }
}