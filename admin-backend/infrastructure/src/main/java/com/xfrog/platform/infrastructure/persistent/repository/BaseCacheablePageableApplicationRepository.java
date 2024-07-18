package com.xfrog.platform.infrastructure.persistent.repository;

import com.xfrog.framework.converter.POToDTOConverter;
import com.xfrog.framework.dto.PageQueryDTO;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.framework.repository.CacheablePageableApplicationRepository;
import com.xfrog.platform.infrastructure.persistent.mapper.PageableMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public abstract class BaseCacheablePageableApplicationRepository<DTO, PO extends AuditPO, M extends PageableMapper<PO, DTO, QueryDTO>, QueryDTO extends PageQueryDTO>
        extends BasePageableApplicationRepository<DTO, PO, M, QueryDTO>
        implements CacheablePageableApplicationRepository<DTO, QueryDTO> {

    public BaseCacheablePageableApplicationRepository(M mapper, POToDTOConverter<PO, DTO> converter) {
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
