package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.Tenant;
import com.xfrog.platform.domain.permission.repository.TenantDomainRepository;
import com.xfrog.platform.infrastructure.permission.converter.TenantPOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.TenantPO;
import com.xfrog.platform.infrastructure.permission.mapper.TenantMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TenantDomainRepositoryImpl extends BaseDomainRepository<Tenant, TenantPO, TenantMapper> implements TenantDomainRepository {

    public TenantDomainRepositoryImpl(TenantMapper tenantMapper) {
        converter = TenantPOConverter.INSTANCE;
        mapper = tenantMapper;
    }

    @Override
    public Tenant findByCode(String code) {
        LambdaQueryWrapper<TenantPO> queryWrapper = new LambdaQueryWrapper<TenantPO>()
                .eq(TenantPO::getDeleted, false)
                .eq(TenantPO::getCode, code);

        TenantPO tenantPO = mapper.selectOne(queryWrapper);
        return converter.toDomain(tenantPO);
    }
}
