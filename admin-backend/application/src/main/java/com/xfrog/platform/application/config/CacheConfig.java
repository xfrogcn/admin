package com.xfrog.platform.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class CacheConfig {

    @Value("${spring.cache.enableGlobalPrefix: true}")
    private boolean useGlobalCachePrefix;

    @Value("${spring.cache.globalPrefix:}")
    private String globalPrefix;

    public static class AdminRedisCacheManagerBuilderCustomizer implements RedisCacheManagerBuilderCustomizer {
        final Jackson2JsonRedisSerializer<Object> serializer;
        public AdminRedisCacheManagerBuilderCustomizer(ObjectMapper objectMapper) {
            serializer =  new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        }

        @Override
        public void customize(RedisCacheManager.RedisCacheManagerBuilder builder) {
            builder.cacheDefaults(
                    builder.cacheDefaults()
                            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            );
        }
    }

    public static class AdminCacheResolver extends AbstractCacheResolver {

        private final boolean useGlobalPrefix;
        private final String globalPrefix;

        public AdminCacheResolver(CacheManager cacheManager, boolean useGlobalPrefix, String globalPrefix) {
            super(cacheManager);
            this.useGlobalPrefix = useGlobalPrefix;
            this.globalPrefix = globalPrefix;
        }

        @Override
        protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
            if (!useGlobalPrefix || !StringUtils.hasText(globalPrefix)) {
                return context.getOperation().getCacheNames();
            }
            Set<String> cacheNames = context.getOperation().getCacheNames();
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
    }

    @Bean
    public AdminRedisCacheManagerBuilderCustomizer adminRedisCacheManagerBuilderCustomizer(ObjectMapper objectMapper) {
        ObjectMapper cacheObjectMapper = objectMapper.copy();
        cacheObjectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        return new AdminRedisCacheManagerBuilderCustomizer(cacheObjectMapper);
    }

    @Bean
    public CachingConfigurer adminCacheConfigurer(CacheManager cacheManager, ApplicationContext applicationContext) {
        return new CachingConfigurer() {
            @Override
            public CacheResolver cacheResolver() {
                String prefix = globalPrefix;
                if (!StringUtils.hasText(prefix)) {
                    prefix = applicationContext.getId();
                }

                return new AdminCacheResolver(cacheManager, useGlobalCachePrefix, prefix);
            }
        };
    }

}
