package com.xfrog.platform.infrastructure.persistent.repository;

import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.framework.dto.IdDTO;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.framework.repository.CacheableApplicationRepository;
import com.xfrog.platform.infrastructure.persistent.cache.BatchKeysCache;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class BaseCacheableApplicationRepository<DTO extends IdDTO, PO extends AuditPO, M extends BaseMapperEx<PO>>
        extends BaseApplicationRepository<DTO, PO, M>
        implements CacheableApplicationRepository<DTO> {

    @Autowired
    protected BatchKeysCache batchKeysCache;

    public BaseCacheableApplicationRepository(M mapper, POToDTOConverter<PO, DTO> converter) {
        super(mapper, converter);
    }

    @Override
    @Cacheable(key = "#p0")
    public DTO queryById(Long id) {
        return super.queryById(id);
    }

    @Override
    public List<DTO> queryByIds(List<Long> ids) {
        return runWithBatchKeyCache(super::queryByIds, ids, IdDTO::getId);
    }

    @Override
    @CacheEvict(key = "#p0")
    public void removeCache(Long id) {
        // nothing
    }

    protected  <KEY, R> Map<KEY, R> runWithBatchKeyCache(Function<List<KEY>, Map<KEY, R>> dbQuery,
                                                 List<KEY> keys) {
        return batchKeysCache.runWithBatchKeyCache(getCacheName(), dbQuery, keys);
    }

    protected  <KEY, R> List<R> runWithBatchKeyCache(Function<List<KEY>, List<R>> dbQuery,
                                                     List<KEY> keys,
                                                     Function<R, KEY> keyGetter) {
        return batchKeysCache.runWithBatchKeyCache(getCacheName(), dbQuery, keys, keyGetter);
    }
}
