package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.domain.base.repository.DicItemDomainRepository;
import com.xfrog.platform.infrastructure.base.converter.DicItemPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.DicItemPO;
import com.xfrog.platform.infrastructure.base.mapper.DicItemMapper;
import com.xfrog.platform.infrastructure.persistent.repository.BaseDomainRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class DicItemDomainRepositoryImpl extends BaseDomainRepository<DicItem, DicItemPO, DicItemMapper>
        implements DicItemDomainRepository {

    public DicItemDomainRepositoryImpl(DicItemMapper dicItemMapper) {
        mapper = dicItemMapper;
        converter = DicItemPOConverter.INSTANCE;
    }

    @Override
    public boolean existsByDisplayText(String displayText, List<Long> excludeIds) {
        LambdaQueryWrapper<DicItemPO> queryWrapper = new LambdaQueryWrapper<DicItemPO>()
                .eq(DicItemPO::getDeleted, false)
                .eq(DicItemPO::getDisplayText, displayText);

        if (!CollectionUtils.isEmpty(excludeIds)) {
            queryWrapper = queryWrapper.notIn(DicItemPO::getId, excludeIds);
        }

        return mapper.exists(queryWrapper);
    }
}
