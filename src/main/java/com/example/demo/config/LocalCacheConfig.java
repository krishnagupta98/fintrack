package com.example.demo.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class LocalCacheConfig{

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();


        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(150)             // Pre-allocates memory slots for 150 concurrent profiles
                .maximumSize(5000)                // Hard cap: stores max 5k active users to protect JVM heap
                .expireAfterWrite(10, TimeUnit.MINUTES) // Soft eviction: refreshes data freshness every 10 mins
                .recordStats());

        cacheManager.setCacheNames(Arrays.asList("userExpenses","expenseSummary"));

        return cacheManager;
    }
}
