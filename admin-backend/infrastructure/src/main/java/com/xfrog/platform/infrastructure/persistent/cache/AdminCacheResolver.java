package com.xfrog.platform.infrastructure.persistent.cache;

import com.xfrog.framework.repository.CacheableRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminCacheResolver extends AbstractCacheResolver {

    private final boolean useGlobalPrefix;
    private final String globalPrefix;

    public AdminCacheResolver(CacheManager cacheManager, boolean useGlobalPrefix, String globalPrefix) {
        super(cacheManager);
        this.useGlobalPrefix = useGlobalPrefix;
        this.globalPrefix = globalPrefix;
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        Set<String> cacheNames = context.getOperation().getCacheNames();
        if (CollectionUtils.isEmpty(cacheNames) && context.getTarget() instanceof CacheableRepository cacheableRepository) {
            String name = cacheableRepository.getCacheName();
            if (StringUtils.hasText(name)) {
                cacheNames = Set.of(name);
            }
        }

        if (!useGlobalPrefix || !StringUtils.hasText(globalPrefix)) {
            return cacheNames;
        }

        if (CollectionUtils.isEmpty(cacheNames)) {
            return Set.of(globalPrefix);
        }

        return cacheNames.stream()
                .map(name -> {
                    // 注意：此处仅支持 #p0 参数，其它参数需要自行实现
                    if (name.equalsIgnoreCase("#p0") && context.getArgs().length > 0) {
                        return String.valueOf(context.getArgs()[0]);
                    }
                    return name;
                })
                .map(name -> String.join(":", globalPrefix, name))
                .collect(Collectors.toSet());
    }

    public String getCahceName(String cacheName) {
        if (!useGlobalPrefix || !StringUtils.hasText(globalPrefix)) {
            return cacheName;
        }

        if (!StringUtils.hasText(cacheName)) {
            return globalPrefix;
        }

        return String.join(":", globalPrefix, cacheName);
    }
}