package com.xfrog.platform.domain.base.repository;

import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.repository.DomainRepository;

import java.util.List;

public interface LangDomainRepository extends DomainRepository<Lang> {
    boolean existsByCode(String application, String code);

    List<Lang> findByApplication(String application, Boolean enabled);
}
