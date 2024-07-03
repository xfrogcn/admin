package com.xfrog.platform.domain.permission.repository;

import com.xfrog.platform.domain.permission.aggregate.Organization;
import com.xfrog.platform.domain.repository.DomainRepository;

import java.util.List;


public interface OrganizationDomainRepository extends DomainRepository<Organization> {
    Integer getMaxSeq(Long parentId);

    boolean existsByName(String name, Long parentId, List<Long> excludeIds);

    boolean existsChildren(Long parentId);
}
