package com.xfrog.framework.repository;

import java.util.List;

public interface DomainRepository<D> {
    D save(D domain);

    List<D> saveAll(List<D> domains);

    D findById(Long id);

    List<D> findByIds(List<Long> ids);

    void logicDelete(Long id);

    void logicDeleteAll(List<D> entities);
}