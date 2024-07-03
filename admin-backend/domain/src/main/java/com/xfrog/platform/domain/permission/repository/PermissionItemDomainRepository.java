package com.xfrog.platform.domain.permission.repository;

import com.xfrog.platform.domain.permission.aggregate.PermissionItem;
import com.xfrog.platform.domain.repository.DomainRepository;

import java.util.List;

public interface PermissionItemDomainRepository extends DomainRepository<PermissionItem> {
    boolean existsByCode(String code, List<Long> excludeIds);

    boolean existsChildren(Long parentId);
}
