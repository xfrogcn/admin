package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.RolePermissionItem;
import com.xfrog.platform.domain.permission.repository.RolePermissionItemDomainRepository;
import com.xfrog.platform.infrastructure.permission.converter.RolePermissionItemPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.RolePermissionItemPO;
import com.xfrog.platform.infrastructure.permission.mapper.RolePermissionItemMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RolePermissionItemDomainRepositoryImpl extends BaseDomainRepository<RolePermissionItem, RolePermissionItemPO, RolePermissionItemMapper> implements RolePermissionItemDomainRepository {
    public RolePermissionItemDomainRepositoryImpl(RolePermissionItemMapper rolePermissionItemMapper) {
        converter = RolePermissionItemPOConverter.INSTANCE;
        mapper = rolePermissionItemMapper;
    }

    @Override
    public List<RolePermissionItem> getByRoleId(Long roleId) {
        List<RolePermissionItemPO> pos = mapper.selectList(new LambdaQueryWrapper<RolePermissionItemPO>()
                .eq(RolePermissionItemPO::getDeleted, false)
                .eq(RolePermissionItemPO::getRoleId, roleId));
        return converter.toDomainList(pos);
    }

    @Override
    public List<RolePermissionItem> getByPermissionItemId(Long permissionItemId) {
        List<RolePermissionItemPO> pos = mapper.selectList(new LambdaQueryWrapper<RolePermissionItemPO>()
                .eq(RolePermissionItemPO::getDeleted, false)
                .eq(RolePermissionItemPO::getPermissionItemId, permissionItemId));
        return converter.toDomainList(pos);
    }
}