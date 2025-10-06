package com.example.project.controller;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTestRunner implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    public RedisTestRunner(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Set a test key
        redisTemplate.opsForValue().set("spring_test_key", "Hello Redis!");

        // Get the key
        String value = redisTemplate.opsForValue().get("spring_test_key");
        System.out.println("Value of spring_test_key: " + value);
    }
}
