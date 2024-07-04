package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.DicPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.DicPO;
import com.xfrog.platform.infrastructure.base.mapper.DicMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class DicDomainRepositoryImpl extends BaseDomainRepository<Dic, DicPO, DicMapper>
        implements DicDomainRepository {
    public DicDomainRepositoryImpl(DicMapper dicMapper) {
        mapper = dicMapper;
        converter = DicPOConverter.INSTANCE;
    }

    @Override
    public boolean existsByTypeOrName(String type, String name, List<Long> excludeIds) {
        LambdaQueryWrapper<DicPO> queryWrapper = new LambdaQueryWrapper<DicPO>()
                .eq(DicPO::getDeleted, false);
        queryWrapper = queryWrapper.and(wrapper -> {
            wrapper.eq(DicPO::getType, type);
            wrapper.or();
            wrapper.eq(DicPO::getName, name);
        });

        if (!CollectionUtils.isEmpty(excludeIds)) {
            queryWrapper = queryWrapper.notIn(DicPO::getId, excludeIds);
        }

        return mapper.exists(queryWrapper);
    }
}
