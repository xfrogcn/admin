package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.DicPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.DicPO;
import com.xfrog.platform.infrastructure.base.mapper.DicMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DicDomainRepositoryImpl extends BaseDomainRepository<Dic, DicPO, DicMapper>
        implements DicDomainRepository {
    public DicDomainRepositoryImpl(DicMapper dicMapper) {
        mapper = dicMapper;
        converter = DicPOConverter.INSTANCE;
    }
}
