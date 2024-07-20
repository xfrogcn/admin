package com.xfrog.platform.application.biz.service;

import com.xfrog.platform.application.biz.dto.CacheDTO;

import java.util.List;

public interface CacheManagerService {
    List<CacheDTO> listCaches();

    void clearCaches(String cacheName);
}
