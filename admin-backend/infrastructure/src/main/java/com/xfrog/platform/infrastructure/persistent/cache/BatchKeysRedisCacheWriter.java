package com.xfrog.platform.infrastructure.persistent.cache;

import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.BatchStrategy;
import org.springframework.data.redis.cache.CacheStatistics;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.FixedDurationTtlFunction;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BatchKeysRedisCacheWriter {

    private static final boolean REACTIVE_REDIS_CONNECTION_FACTORY_PRESENT = ClassUtils
            .isPresent("org.springframework.data.redis.connection.ReactiveRedisConnectionFactory", null);


    private final BatchStrategy batchStrategy;

    private final CacheStatisticsCollector statistics;

    private final Duration sleepTime;

    private final RedisConnectionFactory connectionFactory;

    private final RedisCacheWriter.TtlFunction lockTtl;


    public BatchKeysRedisCacheWriter(RedisConnectionFactory connectionFactory) {
        this(connectionFactory, Duration.ZERO, BatchStrategies.keys());
    }

    /**
     * @param connectionFactory must not be {@literal null}.
     * @param sleepTime sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *          to disable locking.
     * @param batchStrategy must not be {@literal null}.
     */
    BatchKeysRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime, BatchStrategy batchStrategy) {
        this(connectionFactory, sleepTime, new FixedDurationTtlFunction(Duration.ZERO), CacheStatisticsCollector.none(), batchStrategy);
    }

    /**
     * @param connectionFactory must not be {@literal null}.
     * @param sleepTime sleep time between lock request attempts. Must not be {@literal null}. Use {@link Duration#ZERO}
     *          to disable locking.
     * @param lockTtl Lock TTL function must not be {@literal null}.
     * @param cacheStatisticsCollector must not be {@literal null}.
     * @param batchStrategy must not be {@literal null}.
     */
    BatchKeysRedisCacheWriter(RedisConnectionFactory connectionFactory, Duration sleepTime, RedisCacheWriter.TtlFunction lockTtl,
                              CacheStatisticsCollector cacheStatisticsCollector, BatchStrategy batchStrategy) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
        Assert.notNull(sleepTime, "SleepTime must not be null");
        Assert.notNull(lockTtl, "Lock TTL Function must not be null");
        Assert.notNull(cacheStatisticsCollector, "CacheStatisticsCollector must not be null");
        Assert.notNull(batchStrategy, "BatchStrategy must not be null");

        this.connectionFactory = connectionFactory;
        this.sleepTime = sleepTime;
        this.lockTtl = lockTtl;
        this.statistics = cacheStatisticsCollector;
        this.batchStrategy = batchStrategy;
    }

    public List<byte[]> batchGet(String name, byte[][] keys) {
        return batchGet(name, keys, null);
    }

    public List<byte[]> batchGet(String name, byte[][] keys, @Nullable Duration ttl) {

        Assert.notNull(name, "Name must not be null");
        Assert.notNull(keys, "Key must not be null");

        List<byte[]> results = execute(name, connection -> connection.stringCommands().mGet(keys));

        statistics.incGets(name);

        if (results.size() == keys.length) {
            statistics.incHits(name);
        } else {
            statistics.incMisses(name);
        }

        return results;
    }

    public void putBatch(String name, Map<byte[], byte[]> values) {

        Assert.notNull(name, "Name must not be null");
        Assert.notNull(values, "Value must not be null");

        if (values.isEmpty()) {
            return;
        }

        execute(name, connection -> {
            connection.stringCommands().mSet(values);
            return "OK";
        });

        statistics.incPuts(name);
    }


    public CacheStatistics getCacheStatistics(String cacheName) {
        return statistics.getCacheStatistics(cacheName);
    }


    public void clearStatistics(String name) {
        statistics.reset(name);
    }


    public BatchKeysRedisCacheWriter withStatisticsCollector(CacheStatisticsCollector cacheStatisticsCollector) {
        return new BatchKeysRedisCacheWriter(connectionFactory, sleepTime, lockTtl, cacheStatisticsCollector,
                this.batchStrategy);
    }

    /**
     * Explicitly set a write lock on a cache.
     *
     * @param name the name of the cache to lock.
     */
    void lock(String name) {
        execute(name, connection -> doLock(name, name, null, connection));
    }

    @Nullable
    private Boolean doLock(String name, Object contextualKey, @Nullable Object contextualValue,
                           RedisConnection connection) {

        Expiration expiration = Expiration.from(this.lockTtl.getTimeToLive(contextualKey, contextualValue));

        return connection.stringCommands().set(createCacheLockKey(name), new byte[0], expiration, RedisStringCommands.SetOption.SET_IF_ABSENT);
    }

    /**
     * Explicitly remove a write lock from a cache.
     *
     * @param name the name of the cache to unlock.
     */
    void unlock(String name) {
        executeLockFree(connection -> doUnlock(name, connection));
    }

    @Nullable
    private Long doUnlock(String name, RedisConnection connection) {
        return connection.keyCommands().del(createCacheLockKey(name));
    }

    private <T> T execute(String name, Function<RedisConnection, T> callback) {

        try (RedisConnection connection = this.connectionFactory.getConnection()) {
            checkAndPotentiallyWaitUntilUnlocked(name, connection);
            return callback.apply(connection);
        }
    }

    private <T> T executeLockFree(Function<RedisConnection, T> callback) {

        try (RedisConnection connection = this.connectionFactory.getConnection()) {
            return callback.apply(connection);
        }
    }

    /**
     * Determines whether this {@link RedisCacheWriter} uses locks during caching operations.
     *
     * @return {@literal true} if {@link RedisCacheWriter} uses locks.
     */
    private boolean isLockingCacheWriter() {
        return !this.sleepTime.isZero() && !this.sleepTime.isNegative();
    }

    private void checkAndPotentiallyWaitUntilUnlocked(String name, RedisConnection connection) {

        if (!isLockingCacheWriter()) {
            return;
        }

        long lockWaitTimeNs = System.nanoTime();

        try {
            while (doCheckLock(name, connection)) {
                Thread.sleep(this.sleepTime.toMillis());
            }
        } catch (InterruptedException ex) {

            // Re-interrupt current Thread to allow other participants to react.
            Thread.currentThread().interrupt();

            String message = String.format("Interrupted while waiting to unlock cache %s", name);

            throw new PessimisticLockingFailureException(message, ex);
        } finally {
            this.statistics.incLockTime(name, System.nanoTime() - lockWaitTimeNs);
        }
    }

    boolean doCheckLock(String name, RedisConnection connection) {
        return ObjectUtils.nullSafeEquals(connection.keyCommands().exists(createCacheLockKey(name)), true);
    }

    byte[] createCacheLockKey(String name) {
        return (name + "~lock").getBytes(StandardCharsets.UTF_8);
    }

    private static boolean shouldExpireWithin(@Nullable Duration ttl) {
        return ttl != null && !ttl.isZero() && !ttl.isNegative();
    }

}
