package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.LangCorpusPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangCorpusPO;
import com.xfrog.platform.infrastructure.base.mapper.LangCorpusMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LangCorpusDomainRepositoryImpl extends BaseDomainRepository<LangCorpus, LangCorpusPO, LangCorpusMapper>
        implements LangCorpusDomainRepository {
    public LangCorpusDomainRepositoryImpl(LangCorpusMapper langCorpusMapper) {
        mapper = langCorpusMapper;
        converter =LangCorpusPOConverter.INSTANCE;
    }
}