package com.xfrog.platform.infrastructure.persistent.repository;

import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.framework.repository.CacheableApplicationRepository;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public abstract class BaseCacheableApplicationRepository<DTO, PO extends AuditPO, M extends BaseMapperEx<PO>>
        extends BaseApplicationRepository<DTO, PO, M>
        implements CacheableApplicationRepository<DTO> {

    public BaseCacheableApplicationRepository(M mapper, POToDTOConverter<PO, DTO> converter) {
        super(mapper, converter);
    }

    @Override
    @Cacheable(key = "#p0")
    public DTO queryById(Long id) {
        return super.queryById(id);
    }

    @Override
    @CacheEvict(key = "#p0")
    public void removeCache(Long id) {
        // nothing
    }
}
