package com.xfrog.platform.infrastructure.persistent.repository;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xfrog.framework.converter.DomainAndPOConverter;
import com.xfrog.framework.po.AuditPO;
import com.xfrog.platform.domain.repository.DomainRepository;
import com.xfrog.platform.infrastructure.persistent.mapper.BaseMapperEx;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseDomainRepository<D, P extends AuditPO, M extends BaseMapperEx<P>> implements DomainRepository<D> {
    protected DomainAndPOConverter<D, P> converter;

    protected M mapper;

    public D save(D domain) {
        if (domain == null) {
            return null;
        }
        P po = converter.toPO(domain);
        mapper.save(po);
        return converter.toDomain(po);
    }

    public List<D> saveAll(List<D> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return new LinkedList<>();
        }
        List<P> pos = converter.toPOList(domains);
        mapper.saveAll(pos);
        return converter.toDomainList(pos);
    }

    public D findById(Long id) {
        QueryWrapper<P> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("deleted", false);
        return converter.toDomain(mapper.selectOne(queryWrapper));
    }

    public List<D> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new LinkedList<>();
        }
        QueryWrapper<P> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        queryWrapper.eq("deleted", false);
        return converter.toDomainList(mapper.selectList(queryWrapper));
    }

    @Override
    public void logicDelete(Long id) {
        P po = mapper.selectById(id);
        if (po != null) {
            po.setDeleted(true);
            mapper.updateById(po);
        }
    }

    @Override
    public void logicDeleteAll(List<D> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        List<P> entityPos = converter.toPOList(entities);
        entityPos.forEach(po -> po.setDeleted(true));
        mapper.saveAll(entityPos);
    }
}
