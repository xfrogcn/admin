package com.xfrog.platform.domain.base.repository;

import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.framework.repository.DomainRepository;

import java.util.List;

public interface LangCorpusDomainRepository extends DomainRepository<LangCorpus> {
    List<LangCorpus> findAllByApplication(String application);

    boolean existsByApplicationAndCodes(String application, List<String> codes);
}
