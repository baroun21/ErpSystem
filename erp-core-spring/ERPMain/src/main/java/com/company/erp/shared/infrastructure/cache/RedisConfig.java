package com.company.erp.shared.infrastructure.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.time.Duration;

/**
 * Configures Redis for:
 *  1. Distributed caching (@Cacheable, @CacheEvict)
 *  2. HTTP session storage (replaces in-memory HttpSession)
 *  3. Cache-aside pattern for frequently accessed data
 *
 * Benefits:
 *  - Scales across multiple instances
 *  - Reduces database load
 *  - Survives application restart
 */
@Configuration
@EnableCaching
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)  // 30 minutes
@Slf4j
public class RedisConfig {

    /**
     * Configure Redis cache manager with sensible defaults
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        log.info("Initializing Redis cache manager");

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))  // Default TTL: 10 minutes
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.create(connectionFactory);
    }

    /**
     * Note: RedisConnectionFactory is auto-configured by Spring Boot
     * when spring.redis.* properties are set in application.yml
     */
}

