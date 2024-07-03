package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.permission.aggregate.DataScope;
import com.xfrog.platform.domain.permission.repository.DataScopeDomainRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.infrastructure.permission.converter.DataScopePOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.DataScopePO;
import com.xfrog.platform.infrastructure.permission.mapper.DataScopeMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Repository
public class DataScopeDomainRepositoryImpl extends BaseDomainRepository<DataScope, DataScopePO, DataScopeMapper>
        implements DataScopeDomainRepository {
    public DataScopeDomainRepositoryImpl(DataScopeMapper dataScopeMapper) {
        mapper = dataScopeMapper;
        converter = DataScopePOConverter.INSTANCE;
    }

    @Override
    public List<DataScope> findByTargetTypeAndTargetId(DataScopeTargetType targetType, List<Long> targetIds) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return new LinkedList<>();
        }

        List<DataScopePO> dataScopePOS = mapper.selectList(new LambdaQueryWrapper<DataScopePO>()
                .eq(DataScopePO::getDeleted, false)
                .eq(DataScopePO::getTargetType, targetType)
                .in(DataScopePO::getTargetId, targetIds));

        return converter.toDomainList(dataScopePOS);
    }
}
