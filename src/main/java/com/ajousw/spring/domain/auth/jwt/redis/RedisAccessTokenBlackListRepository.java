package com.ajousw.spring.domain.auth.jwt.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisAccessTokenBlackListRepository {
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    public Object getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public boolean isKeyBlackList(String key) {
        return redisBlackListTemplate.hasKey(key);
    }
}