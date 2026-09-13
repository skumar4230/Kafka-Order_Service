package com.example.kafkaorders.product;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ProductCacheService {

    private final StringRedisTemplate redis;

    public ProductCacheService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public String getProduct(String id) {
        String cached = redis.opsForValue().get("product:" + id);
        if (cached != null) {
            return "CACHE HIT: " + cached;
        }

        // Demo data source. In a real system this would be PostgreSQL.
        String value = "{\"id\":\"" + id + "\",\"name\":\"Demo Product\",\"price\":49999}";
        redis.opsForValue().set("product:" + id, value, Duration.ofMinutes(10));
        return "CACHE MISS -> DB -> REDIS: " + value;
    }
}
