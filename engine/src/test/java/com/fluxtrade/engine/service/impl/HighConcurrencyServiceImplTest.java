package com.fluxtrade.engine.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class HighConcurrencyServiceImplTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock // 模擬 Redis 的操作物件
    private ValueOperations<String, String> valueOperations;

    @InjectMocks // 自動將上面兩個 Mock 物件注入到這個實作類中
    private HighConcurrencyServiceImpl highConcurrencyService;

    @Test
    void initStock() {
    }

    @Test
    void reduceStock2() {
    }
}