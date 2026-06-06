package com.example.demo.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterService.class);

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_SIZE_MS = 60000;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {
        String key = "rate_limit:" + userId;
        long now = System.currentTimeMillis();
        long windowStart = now - WINDOW_SIZE_MS;

        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart);
        Long currentCount = redisTemplate.opsForZSet().zCard(key);
        redisTemplate.expire(key, Duration.ofMinutes(2));

        if (currentCount != null && currentCount >= MAX_REQUESTS) {
            log.warn("Rate limit exceeded for user: {}", userId);
            return false;
        }

        redisTemplate.opsForZSet().add(key,String.valueOf(now),now);

        return true;
    }
}