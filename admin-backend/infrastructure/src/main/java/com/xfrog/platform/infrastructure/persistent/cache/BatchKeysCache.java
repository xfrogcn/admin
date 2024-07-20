package com.xfrog.platform.infrastructure.persistent.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NullValue;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class BatchKeysCache {

     private final ObjectProvider<CacheManager> cacheManagerProvider;
     private final ObjectProvider<RedisConnectionFactory> redisConnectionFactoryProvider;
     private final AdminCacheResolver adminCacheResolver;

     private final ConversionService conversionService = new DefaultFormattingConversionService();
    static final byte[] BINARY_NULL_VALUE = RedisSerializer.java().serialize(NullValue.INSTANCE);

     private volatile BatchKeysRedisCacheWriter batchKeysRedisCacheWriter;

    /**
     *  批量缓存，用于增强queryByKeys方法，根据传入的Key列表，先批量获取缓存，再将无缓存的Key类别传递给dbQuery
     *  根据dbQuery查询结果自动更新对应Key的缓存
     * @param cacheName 缓存名称
     * @param dbQuery 数据库查询方法
     * @param keys 查询Key列表
     * @param keyGetter 从查询结果中获取key的方法
     * @return 查询结果
     * @param <R> 查询结果类型
     * @param <KEY> 查询Key类型
     */
    public <R, KEY> List<R> runWithBatchKeyCache(String cacheName,
                                                 Function<List<KEY>, List<R>> dbQuery,
                                                 List<KEY> keys,
                                                 Function<R, KEY> keyGetter) {
        return runWithBatchKeyCache(cacheName, (key) -> conversionService.convert(key, String.class), dbQuery, keys, keyGetter);
    }

    /**
     *  批量缓存，用于增强queryByKeys方法，根据传入的Key列表，先批量获取缓存，再将无缓存的Key类别传递给dbQuery
     *  根据dbQuery查询结果自动更新对应Key的缓存
     * @param cacheName 缓存名称
     * @param cacheKeyGetter 缓存Key获取方法
     * @param dbQuery 数据库查询方法
     * @param keys 查询Key列表
     * @param keyGetter 从查询结果中获取key的方法
     * @return 查询结果
     * @param <R> 查询结果类型
     * @param <KEY> 查询Key类型
     */
     public <R, KEY> List<R> runWithBatchKeyCache(String cacheName,
                                                  Function<KEY, String> cacheKeyGetter,
                                                  Function<List<KEY>, List<R>> dbQuery,
                                                  List<KEY> keys,
                                                  Function<R, KEY> keyGetter) {

         Assert.notNull(cacheKeyGetter, "cacheKeyGetter must not be null");
         Assert.notNull(dbQuery, "dbQuery must not be null");
         Assert.notNull(keyGetter, "keyGetter must not be null");

        if (CollectionUtils.isEmpty(keys)) {
            return dbQuery.apply(keys);
        }

         CacheManager cacheManager = this.cacheManagerProvider.getIfAvailable();
         RedisConnectionFactory redisConnectionFactory = this.redisConnectionFactoryProvider.getIfAvailable();

         // 只有在Redis缓存时，使用批量Key缓存
         if (!(cacheManager instanceof RedisCacheManager redisCacheManager) || redisConnectionFactory == null) {
             return dbQuery.apply(keys);
         }

         // 初始化writer
         if (this.batchKeysRedisCacheWriter == null) {
             synchronized (this) {
                 if (this.batchKeysRedisCacheWriter == null) {
                     this.batchKeysRedisCacheWriter = new BatchKeysRedisCacheWriter(redisConnectionFactory);
                 }
             }
         }

         // 从缓存读取
         RedisCache redisCache = (RedisCache) redisCacheManager.getCache(adminCacheResolver.getCahceName(cacheName));
         if (redisCache == null) {
             log.warn("redis cache is null!");
             return dbQuery.apply(keys);
         }
         RedisCacheConfiguration cacheConfiguration = redisCache.getCacheConfiguration();

         String actualCacheName = redisCache.getName();
         Map<KEY, byte[]> keyMaps = new HashMap<>();
         byte[][] keyBytes = keys.stream().map(key -> {
             byte[] keyByte = createAndConvertCacheKey(actualCacheName, cacheKeyGetter.apply(key), cacheConfiguration);
             keyMaps.put(key, keyByte);
             return keyByte;
         }).toArray(byte[][]::new);

         List<byte[]> cacheResultBytes = this.batchKeysRedisCacheWriter.batchGet(actualCacheName, keyBytes);
         Set<KEY> keySets = new HashSet<>(keys);
         List<R> results = new LinkedList<>();
         for (int i = 0; i < cacheResultBytes.size(); i++) {
             byte[] cacheResultByte = cacheResultBytes.get(i);
             if (cacheResultByte != null) {
                 Object cacheResult = deserializeCacheValue(redisCache.isAllowNullValues(),
                         cacheConfiguration, cacheResultByte);
                 if (cacheResult != null) {
                     keySets.remove(keys.get(i));
                     if (cacheResult != NullValue.INSTANCE) {
                         results.add((R) cacheResult);
                     }
                 }
            }
        }

         if (keySets.isEmpty()) {
             // 缓存都存在
             return results;
         }

         List<R> dbResults = dbQuery.apply(keySets.stream().toList());
         Map<byte[], byte[]> cacheValues = new HashMap<>(keySets.size());
         if (dbResults != null) {
             dbResults.forEach(dbResult -> {
                 KEY key = keyGetter.apply(dbResult);

                 results.add(dbResult);

                 byte[] cacheValue = serializeCacheValue(redisCache.isAllowNullValues(), cacheConfiguration, dbResult);
                 cacheValues.put(keyMaps.get(key), cacheValue);

                 keySets.remove(key);
             });
         }
         if (!keySets.isEmpty()) {
             // 数据库不存在，写入null值
             cacheValues.putAll(
                     keySets.stream()
                             .collect(HashMap::new,
                                     (map, key) -> map.put(keyMaps.get(key), BINARY_NULL_VALUE),
                                     HashMap::putAll));
         }

         batchKeysRedisCacheWriter.putBatch(actualCacheName, cacheValues);

         return results;
     }

    private byte[] createAndConvertCacheKey(String cacheName, String key, RedisCacheConfiguration cacheConfiguration) {
        String cacheKey = cacheConfiguration.usePrefix() ? cacheConfiguration.getKeyPrefixFor(cacheName) + key : key;
        return ByteUtils.getBytes(cacheConfiguration.getKeySerializationPair().write(cacheKey));
    }

    protected Object deserializeCacheValue(boolean isNullable, RedisCacheConfiguration cacheConfiguration, byte[] value) {

        if (isNullable && ObjectUtils.nullSafeEquals(value, BINARY_NULL_VALUE)) {
            return NullValue.INSTANCE;
        }

        return cacheConfiguration.getValueSerializationPair().read(ByteBuffer.wrap(value));
    }

    protected byte[] serializeCacheValue(boolean isNullable, RedisCacheConfiguration cacheConfiguration, Object value) {

        if (isNullable && value instanceof NullValue) {
            return BINARY_NULL_VALUE;
        }

        return ByteUtils.getBytes(cacheConfiguration.getValueSerializationPair().write(value));
    }
}
