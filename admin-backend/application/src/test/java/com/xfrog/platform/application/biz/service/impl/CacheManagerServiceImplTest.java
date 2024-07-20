package com.xfrog.platform.application.biz.service.impl;

import com.xfrog.platform.application.biz.dto.CacheDTO;
import com.xfrog.platform.application.biz.repository.CacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class )
class CacheManagerServiceImplTest {

    @Mock
    private CacheRepository cacheRepository;

    @InjectMocks
    private CacheManagerServiceImpl cacheManagerService;

    @Test
    void listCaches_ShouldReturnAllCaches() {
        List<CacheDTO> caches = cacheManagerService.listCaches();

        assertThat(caches).hasSize(4);
    }

    @Test
    void clearCaches_givenCacheNameWhenClearCachesThenCorrectCachesAreCleared() {
        // Arrange
        String cacheName = "user:detail";

        // Act
        cacheManagerService.clearCaches(cacheName);

        // Assert
        verify(cacheRepository, times(2)).clearCaches(anyString());

    }

    @Test
    void clearCaches_givenNonexistentCacheNameWhenClearCachesThenNoCachesAreCleared() {
        // Arrange
        String cacheName = "nonexistent";

        // Act
        cacheManagerService.clearCaches(cacheName);

        // Assert
        verify(cacheRepository, never()).clearCaches(anyString());
    }
}