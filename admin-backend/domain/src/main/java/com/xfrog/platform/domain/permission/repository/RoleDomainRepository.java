package com.xfrog.platform.domain.permission.repository;

import com.xfrog.platform.domain.permission.aggregate.Role;
import com.xfrog.framework.repository.DomainRepository;

import java.util.List;

public interface RoleDomainRepository extends DomainRepository<Role> {
    boolean existsByName(String name, List<Long> excludeIds);
}
