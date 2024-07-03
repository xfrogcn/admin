package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.Role;
import com.xfrog.platform.domain.permission.repository.RoleDomainRepository;
import com.xfrog.platform.infrastructure.permission.converter.RolePOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.RolePO;
import com.xfrog.platform.infrastructure.permission.mapper.RoleMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class RoleDomainRepositoryImpl extends BaseDomainRepository<Role, RolePO, RoleMapper> implements RoleDomainRepository {
    public RoleDomainRepositoryImpl(RoleMapper roleMapper) {
        converter = RolePOConverter.INSTANCE;
        mapper = roleMapper;
    }

    @Override
    public boolean existsByName(String name, List<Long> excludeIds) {
        LambdaQueryWrapper<RolePO> queryWrapper = new LambdaQueryWrapper<RolePO>()
                .eq(RolePO::getDeleted, false)
                .eq(RolePO::getName, name);

        if (!CollectionUtils.isEmpty(excludeIds)) {
            queryWrapper = queryWrapper.notIn(RolePO::getId, excludeIds);
        }

        RolePO rolePO = mapper.selectOne(queryWrapper);

        return rolePO != null;
    }
}
