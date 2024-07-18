package com.xfrog.platform.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class CacheConfig {
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

    @Bean
    public AdminRedisCacheManagerBuilderCustomizer adminRedisCacheManagerBuilderCustomizer(ObjectMapper objectMapper) {
        ObjectMapper cacheObjectMapper = objectMapper.copy();
        cacheObjectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        return new AdminRedisCacheManagerBuilderCustomizer(cacheObjectMapper);
    }

}
