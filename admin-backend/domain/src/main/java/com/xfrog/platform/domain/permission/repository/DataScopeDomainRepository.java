package com.xfrog.platform.domain.permission.repository;

import com.xfrog.platform.domain.permission.aggregate.DataScope;
import com.xfrog.platform.domain.repository.DomainRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;

import java.util.List;

public interface DataScopeDomainRepository extends DomainRepository<DataScope> {
    List<DataScope> findByTargetTypeAndTargetId(DataScopeTargetType targetType, List<Long> targetIds);
}
