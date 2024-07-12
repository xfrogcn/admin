package com.xfrog.platform.domain.permission.repository;

import com.xfrog.platform.domain.permission.aggregate.Tenant;
import com.xfrog.framework.repository.DomainRepository;

public interface TenantDomainRepository extends DomainRepository<Tenant> {
    Tenant findByCode(String code);
}
