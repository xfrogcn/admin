package com.xfrog.framework.repository;

public interface CacheableRepository {
    String getCacheName();

    void removeCache(Long id);
}
