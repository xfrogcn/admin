package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.LangLocalPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangLocalPO;
import com.xfrog.platform.infrastructure.base.mapper.LangLocalMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LangLocalDomainRepositoryImpl extends BaseDomainRepository<LangLocal, LangLocalPO, LangLocalMapper>
        implements LangLocalDomainRepository {
    public LangLocalDomainRepositoryImpl(LangLocalMapper langLocalMapper) {
        mapper = langLocalMapper;
        converter = LangLocalPOConverter.INSTANCE;
    }
}
