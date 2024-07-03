package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import com.xfrog.platform.domain.permission.repository.PermissionItemDomainRepository;
import com.xfrog.platform.infrastructure.permission.converter.PermissionItemPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.PermissionItemPO;
import com.xfrog.platform.infrastructure.permission.mapper.PermissionItemMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class PermissionItemDomainRepositoryImpl extends BaseDomainRepository<PermissionItem, PermissionItemPO, PermissionItemMapper> implements PermissionItemDomainRepository {
    public PermissionItemDomainRepositoryImpl(PermissionItemMapper permissionItemsMapper) {
        converter = PermissionItemPOConverter.INSTANCE;
        mapper = permissionItemsMapper;
    }

    @Override
    public boolean existsByCode(String code, List<Long> excludeIds) {
        LambdaQueryWrapper<PermissionItemPO> queryWrapper = new LambdaQueryWrapper<PermissionItemPO>()
                .eq(PermissionItemPO::getDeleted, false)
                .eq(PermissionItemPO::getCode, code);

        if (!CollectionUtils.isEmpty(excludeIds)) {
            queryWrapper =queryWrapper.notIn(PermissionItemPO::getId, excludeIds);
        }

        PermissionItemPO organizationPO = mapper.selectOne(queryWrapper);

        return organizationPO != null;
    }

    @Override
    public boolean existsChildren(Long parentId) {
        LambdaQueryWrapper<PermissionItemPO> queryWrapper = withParentIdCondition(parentId)
                .eq(PermissionItemPO::getDeleted, false);

        return mapper.exists(queryWrapper);
    }

    private static LambdaQueryWrapper<PermissionItemPO> withParentIdCondition(Long parentId) {
        LambdaQueryWrapper<PermissionItemPO> queryWrapper = new LambdaQueryWrapper<>();
        if (parentId == null) {
            queryWrapper = queryWrapper.isNull(PermissionItemPO::getParentId);
        } else {
            queryWrapper = queryWrapper.eq(PermissionItemPO::getParentId, parentId);
        }
        return queryWrapper;
    }
}
