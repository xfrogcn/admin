package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.base.dto.DicDTO;
import com.xfrog.platform.application.base.dto.DicItemDTO;
import com.xfrog.platform.application.base.dto.QueryDicRequestDTO;
import com.xfrog.platform.application.base.repository.DicRepository;
import com.xfrog.platform.infrastructure.base.common.BaseCacheNames;
import com.xfrog.platform.infrastructure.base.converter.DicItemPOConverter;
import com.xfrog.platform.infrastructure.base.converter.DicPOConverter;
import com.xfrog.platform.infrastructure.base.dataobject.DicItemPO;
import com.xfrog.platform.infrastructure.base.dataobject.DicPO;
import com.xfrog.platform.infrastructure.base.mapper.DicItemMapper;
import com.xfrog.platform.infrastructure.base.mapper.DicMapper;
import com.xfrog.platform.infrastructure.persistent.cache.BatchKeysCache;
import com.xfrog.platform.infrastructure.persistent.repository.BaseCacheablePageableApplicationRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class DicRepositoryImpl extends BaseCacheablePageableApplicationRepository<DicDTO, DicPO, DicMapper, QueryDicRequestDTO>
        implements DicRepository {

    private final DicItemMapper dicItemMapper;
    private final BatchKeysCache batchKeysCache;

    public DicRepositoryImpl(DicMapper dicMapper, DicItemMapper dicItemMapper, BatchKeysCache batchKeysCache) {
        super(dicMapper, DicPOConverter.INSTANCE);
        this.dicItemMapper = dicItemMapper;
        this.batchKeysCache = batchKeysCache;
    }

    private static final CaseInsensitiveMap<String, String> ORDER_FIELD_MAP =
            new CaseInsensitiveMap<>(Map.of(
                    "createdTime", "dic.created_time",
                    "type", "dic.type",
                    "name", "dic.name"));

    @Override
    protected Map<String, String> orderedFieldMap() {
        return ORDER_FIELD_MAP;
    }

    @Override
    public String getCacheName() {
        return BaseCacheNames.DIC_DETAIL;
    }

    @Override
    public List<DicDTO> queryByTypes(List<String> types) {
        if (CollectionUtils.isEmpty(types)) {
            return new LinkedList<>();
        }

        return batchKeysCache.runWithBatchKeyCache(BaseCacheNames.DIC_BY_TYPE, (keys) -> {
            LambdaQueryWrapper<DicPO> queryWrapper = new LambdaQueryWrapper<DicPO>()
                    .in(DicPO::getType, keys)
                    .eq(DicPO::getDeleted, false);

            return DicPOConverter.INSTANCE.toDTOList(mapper.selectList(queryWrapper));
        }, types, DicDTO::getType);
    }

    @Override
    public List<DicItemDTO> queryItemsByDicId(List<Long> dicIds) {
        if (CollectionUtils.isEmpty(dicIds)) {
            return new LinkedList<>();
        }
        return batchKeysCache.runWithBatchKeyCache(BaseCacheNames.DIC_ITEM_BY_DIC_ID, (keys) -> {
            LambdaQueryWrapper<DicItemPO> queryWrapper = new LambdaQueryWrapper<DicItemPO>()
                    .in(DicItemPO::getDicId, dicIds)
                    .eq(DicItemPO::getDeleted, false);

            return DicItemPOConverter.INSTANCE.toDTOList(dicItemMapper.selectList(queryWrapper));
        }, dicIds, DicItemDTO::getId);
    }

    @Override
    @CacheEvict(cacheNames = BaseCacheNames.DIC_BY_TYPE, key = "#p0")
    public void removeDicCacheByType(String type) {
        // nothing
    }

    @Override
    @CacheEvict(cacheNames = BaseCacheNames.DIC_ITEM_BY_DIC_ID, key = "#p0")
    public void removeDicItemsCacheByDicId(Long dicId) {
        // nothing
    }
}