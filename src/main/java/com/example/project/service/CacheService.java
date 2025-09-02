package com.example.project.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Duration TTL = Duration.ofMinutes(10); // cache 10 mins

    public void cacheValue(String key, Object value, Duration duration) {
        if (duration == null) {
            duration = TTL;
        }
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

	

	

	
}
