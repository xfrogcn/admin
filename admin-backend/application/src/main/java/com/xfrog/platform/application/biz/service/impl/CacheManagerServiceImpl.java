package com.xfrog.platform.application.biz.service.impl;

import com.xfrog.platform.application.biz.dto.CacheDTO;
import com.xfrog.platform.application.biz.repository.CacheRepository;
import com.xfrog.platform.application.biz.service.CacheManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheManagerServiceImpl implements CacheManagerService {

    private final CacheRepository cacheRepository;

    private final Map<String, List<String>> cacheNameMap = Map.of(
            "user:detail", List.of("user:detail", "tenant:by-code"),
            "user:permission", List.of("user:permission-codes", "user:role-ids"),
            "user:data-scope", List.of("user:permission-codes", "user:role-ids"),
            "dic:detail", List.of("dic:detail", "dic:by-type", "dic-item:by-dic-id")
    );

    private final List<CacheDTO> cacheList = List.of(
            CacheDTO.builder()
                    .cacheName("user:detail")
                    .displayText("用户信息")
                    .displayCorpusCode("admin.ui.pages.cache-user-detail").build(),
            CacheDTO.builder()
                    .cacheName("user:permission")
                    .displayText("用户功能权限")
                    .displayCorpusCode("admin.ui.pages.cache-user-permission").build(),
            CacheDTO.builder()
                    .cacheName("user:data-scope")
                    .displayText("用户数据权限")
                    .displayCorpusCode("admin.ui.pages.cache-user-data-scope").build(),
            CacheDTO.builder()
                    .cacheName("dic:detail")
                    .displayText("数据字典")
                    .displayCorpusCode("admin.ui.pages.cache-dic-detail").build()
    );

    @Override
    public List<CacheDTO> listCaches() {
        return cacheList.stream().toList();
    }

    @Override
    public void clearCaches(String cacheName) {
        List<String> cacheList = cacheNameMap.get(cacheName);
        if (CollectionUtils.isEmpty(cacheList)) {
            return;
        }
        cacheList.forEach(cacheRepository::clearCaches);
    }
}
