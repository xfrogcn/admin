package com.xfrog.platform.infrastructure.persistent.repository;

import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.framework.dto.IdDTO;
import com.xfrog.framework.dto.PageQueryDTO;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.framework.repository.CacheablePageableApplicationRepository;
import com.xfrog.platform.infrastructure.persistent.cache.BatchKeysCache;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.function.Function;

public abstract class BaseCacheablePageableApplicationRepository<DTO extends IdDTO, PO extends AuditPO, M extends PageableMapper<PO, DTO, QueryDTO>, QueryDTO extends PageQueryDTO>
        extends BasePageableApplicationRepository<DTO, PO, M, QueryDTO>
        implements CacheablePageableApplicationRepository<DTO, QueryDTO> {

    @Autowired
    protected BatchKeysCache batchKeysCache;

    public BaseCacheablePageableApplicationRepository(M mapper, POToDTOConverter<PO, DTO> converter) {
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

    protected  <R, KEY> List<R> runWithBatchKeyCache(Function<List<KEY>, List<R>> dbQuery,
                                                     List<KEY> keys,
                                                     Function<R, KEY> keyGetter) {
        return batchKeysCache.runWithBatchKeyCache(getCacheName(), dbQuery, keys, keyGetter);
    }
}
