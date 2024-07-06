package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.LangPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.LangPO;
import com.xfrog.platform.infrastructure.base.mapper.LangMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LangDomainRepositoryImpl extends BaseDomainRepository<Lang, LangPO, LangMapper>
        implements LangDomainRepository {
    public LangDomainRepositoryImpl(LangMapper langMapper) {
        mapper = langMapper;
        converter = LangPOConverter.INSTANCE;
    }
}
