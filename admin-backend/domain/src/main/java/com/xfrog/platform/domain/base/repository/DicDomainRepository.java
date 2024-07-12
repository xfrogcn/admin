package com.xfrog.platform.domain.base.repository;

import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.framework.repository.DomainRepository;

import java.util.List;

public interface DicDomainRepository extends DomainRepository<Dic> {
    boolean existsByTypeOrName(String type, String name, List<Long> excludeIds);
}