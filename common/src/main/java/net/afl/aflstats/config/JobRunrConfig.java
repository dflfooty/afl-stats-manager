package net.afl.aflstats.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.spring.autoconfigure.JobRunrProperties;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.nosql.redis.LettuceRedisStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;

@Configuration
public class JobRunrConfig {

    @Value("${spring.redis.url:redis://localhost:6379}") String redisUrl;
    @Value("${spring.redis.lettuce.pool.max-active:8}") int poolMaxActive;
    @Value("${spring.redis.lettuce.pool.max-idle:8}") int poolMaxIdle;
    @Value("${spring.redis.lettuce.pool.max-wait:-1ms}") Duration poolMaxWait;
    @Value("${spring.redis.lettuce.pool.min-idle:0}") int poolMinIdle;
    @Value("${spring.redis.lettuce.pool.time-between-eviction-runs:-1ms}") Duration poolEvictionTime;
    
    @Bean
    public StorageProvider altStorageProvider(JobMapper jobMapper, JobRunrProperties properties) {
        RedisClient redisClient = RedisClient.create(this.redisUrl);
        GenericObjectPoolConfig<StatefulRedisConnection<String, String>> poolConfig = new GenericObjectPoolConfig<>();
        
        poolConfig.setMaxTotal(this.poolMaxActive);
        poolConfig.setMaxIdle(this.poolMaxIdle);
        poolConfig.setMaxWait(this.poolMaxWait);
        poolConfig.setMinIdle(this.poolMinIdle);
        poolConfig.setMinEvictableIdleTime(this.poolEvictionTime);

        GenericObjectPool<StatefulRedisConnection<String, String>> pool = ConnectionPoolSupport.createGenericObjectPool(() -> redisClient.connect(), poolConfig);

        LettuceRedisStorageProvider lettuceRedisStorageProvider = new LettuceRedisStorageProvider(pool, properties.getDatabase().getTablePrefix());
        lettuceRedisStorageProvider.setJobMapper(jobMapper);

        return lettuceRedisStorageProvider;
    }
}
