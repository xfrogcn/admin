package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.framework.oplog.domain.OpLog;
import com.xfrog.platform.domain.base.repository.OpLogDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.OpLogPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.OpLogPO;
import com.xfrog.platform.infrastructure.base.mapper.OpLogMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OpLogDomainRepositoryImpl  extends BaseDomainRepository<OpLog, OpLogPO, OpLogMapper>
        implements OpLogDomainRepository {

    public OpLogDomainRepositoryImpl(OpLogMapper opLogMapper) {
        this.mapper = opLogMapper;
        this.converter = OpLogPOConverter.INSTANCE;
    }
}
