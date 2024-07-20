package com.xfrog.platform.infrastructure.persistent.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.xfrog.platform.infrastructure.persistent.cache.AdminCacheResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.StringUtils;


@Configuration(proxyBeanMethods = false)
public class CacheConfig {

    @Value("${spring.cache.enableGlobalPrefix: true}")
    private boolean useGlobalCachePrefix;

    @Value("${spring.cache.globalPrefix:}")
    private String globalPrefix;

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(ObjectMapper objectMapper) {
        return builder -> {
            RedisCacheConfiguration configuration = builder.cacheDefaults();

            ObjectMapper cacheObjectMapper = objectMapper.copy();
            cacheObjectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            final Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(cacheObjectMapper, Object.class);
            configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

            builder.cacheDefaults(configuration);
        };
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
