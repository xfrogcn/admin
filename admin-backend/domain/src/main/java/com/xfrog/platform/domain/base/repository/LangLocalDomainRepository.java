package com.xfrog.platform.domain.base.repository;

import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.repository.DomainRepository;

import java.util.List;

public interface LangLocalDomainRepository extends DomainRepository<LangLocal> {
    List<LangLocal> findAllByApplicationAndLangId(String application, List<Long> langIds);

    List<LangLocal> findAllByApplicationAndCorpusIds(String application, List<Long> corpusIds);
}
