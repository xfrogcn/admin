package com.xfrog.platform.domain.base.repository;

import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.repository.DomainRepository;

public interface LangDomainRepository extends DomainRepository<Lang> {
    boolean existsByCode(String application, String code);
}
