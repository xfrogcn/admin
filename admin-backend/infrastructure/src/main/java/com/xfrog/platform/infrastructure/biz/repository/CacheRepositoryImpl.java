package com.xfrog.platform.infrastructure.biz.repository;

import com.xfrog.platform.application.biz.repository.CacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CacheRepositoryImpl implements CacheRepository {
    @Override
    @CacheEvict(cacheNames = "#p0", allEntries = true)
    public void clearCaches(String cacheName) {
        log.info("clear cache: {}", cacheName);
    }
}
