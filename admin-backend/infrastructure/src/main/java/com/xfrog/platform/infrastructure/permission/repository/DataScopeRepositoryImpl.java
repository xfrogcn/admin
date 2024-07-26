package com.xfrog.platform.infrastructure.permission.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xfrog.platform.application.permission.dto.DataScopeDTO;
import com.xfrog.platform.application.permission.repository.DataScopeRepository;
import com.xfrog.platform.domain.share.permission.DataScopeTargetType;
import com.xfrog.platform.infrastructure.permission.common.PermissionCacheNames;
import com.xfrog.platform.infrastructure.permission.converter.DataScopePOConverter;
import com.xfrog.platform.infrastructure.permission.dataobject.DataScopePO;
import com.xfrog.platform.infrastructure.permission.mapper.DataScopeMapper;
import com.xfrog.platform.infrastructure.persistent.cache.BatchKeysCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataScopeRepositoryImpl implements DataScopeRepository {

    private final DataScopeMapper mapper;

    private final BatchKeysCache batchKeysCache;

    @Override
    public List<DataScopeDTO> findByTargetTypeAndTargetId(DataScopeTargetType targetType, List<Long> targetIds) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return new LinkedList<>();
        }

        return batchKeysCache.runWithBatchKeyCache(PermissionCacheNames.DATA_SCOPE + targetType.name(), (keys) -> {
            List<DataScopePO> dataScopePOS = mapper.selectList(new LambdaQueryWrapper<DataScopePO>()
                    .eq(DataScopePO::getDeleted, false)
                    .eq(DataScopePO::getTargetType, targetType)
                    .in(DataScopePO::getTargetId, targetIds));

            return DataScopePOConverter.INSTANCE.toDTOList(dataScopePOS);
        }, targetIds, DataScopeDTO::getTargetId);
    }

    @Override
    @CacheEvict(cacheNames = PermissionCacheNames.DATA_SCOPE + "#p0", key = "#p1")
    public void removeCacheByTargetTypeAndTargetId(DataScopeTargetType targetType, Long targetId) {
        // nothing
        log.info("remove data scope cache: {} {}", targetType, targetId);
    }
}
