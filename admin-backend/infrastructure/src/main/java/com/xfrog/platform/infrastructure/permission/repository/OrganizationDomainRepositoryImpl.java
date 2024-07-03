package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.permission.repository.OrganizationDomainRepository;
import com.xfrog.platform.infrastructure.permission.converter.OrganizationPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.OrganizationPO;
import com.xfrog.platform.infrastructure.permission.mapper.OrganizationMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class OrganizationDomainRepositoryImpl extends BaseDomainRepository<Organization, OrganizationPO, OrganizationMapper>
        implements OrganizationDomainRepository {
    public OrganizationDomainRepositoryImpl(OrganizationMapper organizationMapper) {
        mapper = organizationMapper;
        converter = OrganizationPOConverter.INSTANCE;
    }
    @Override
    public Integer getMaxSeq(Long parentId) {
        LambdaQueryWrapper<OrganizationPO> queryWrapper = withParentIdCondition(parentId);
        queryWrapper = queryWrapper.orderByDesc(OrganizationPO::getSeq);
        queryWrapper = queryWrapper.last("limit 1");
        OrganizationPO organizationPO = mapper.selectOne(queryWrapper);
        if (organizationPO == null) {
            return 0;
        }
        return organizationPO.getSeq();
    }

    @Override
    public boolean existsByName(String name, Long parentId, List<Long> excludeIds) {
        LambdaQueryWrapper<OrganizationPO> queryWrapper = withParentIdCondition(parentId)
                .eq(OrganizationPO::getDeleted, false)
                .eq(OrganizationPO::getName, name);

        if (!CollectionUtils.isEmpty(excludeIds)) {
            queryWrapper =queryWrapper.notIn(OrganizationPO::getId, excludeIds);
        }

        OrganizationPO organizationPO = mapper.selectOne(queryWrapper);

        return organizationPO != null;
    }

    @Override
    public boolean existsChildren(Long parentId) {
        LambdaQueryWrapper<OrganizationPO> queryWrapper = withParentIdCondition(parentId)
                .eq(OrganizationPO::getDeleted, false);

        return mapper.exists(queryWrapper);
    }

    private static LambdaQueryWrapper<OrganizationPO> withParentIdCondition(Long parentId) {
        LambdaQueryWrapper<OrganizationPO> queryWrapper = new LambdaQueryWrapper<>();
        if (parentId == null) {
            queryWrapper = queryWrapper.isNull(OrganizationPO::getParentId);
        } else {
            queryWrapper = queryWrapper.eq(OrganizationPO::getParentId, parentId);
        }
        return queryWrapper;
    }
}
