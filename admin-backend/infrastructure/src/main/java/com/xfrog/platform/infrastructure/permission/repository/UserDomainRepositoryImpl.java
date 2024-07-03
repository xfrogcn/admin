package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.User;
import com.xfrog.platform.domain.permission.repository.UserDomainRepository;
import com.xfrog.platform.infrastructure.permission.converter.UserPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.UserPO;
import com.xfrog.platform.infrastructure.permission.mapper.UserMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class UserDomainRepositoryImpl extends BaseDomainRepository<User, UserPO, UserMapper> implements UserDomainRepository {
    public UserDomainRepositoryImpl(UserMapper userMapper) {
        converter = UserPOConverter.INSTANCE;
        mapper = userMapper;
    }

    @Override
    public User findByUserName(String userName) {
        LambdaQueryWrapper<UserPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPO::getUserName, userName);
        queryWrapper.eq(UserPO::getDeleted, false);
        UserPO userPO = mapper.selectOne(queryWrapper);
        return converter.toDomain(userPO);
    }

    @Override
    public void updateLastLoginTime(Long userId, LocalDateTime lastLoginTime) {
        mapper.updateLastLoginTime(userId, lastLoginTime);
    }
}
