package com.fluxtrade.engine.service.impl;


import com.fluxtrade.engine.service.HighConcurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class HighConcurrencyServiceImpl implements HighConcurrencyService {

    private final StringRedisTemplate redisTemplate;
    private static final String STOCK = "stock";

    @Override
    public String initStock() {
        redisTemplate.opsForValue().set(STOCK, "100");
        return "庫存已重置為 100！";
    }

    @Override
    public String reduceStock1() {
        String stockStr = redisTemplate.opsForValue().get(STOCK);
        log.info(stockStr);
        int stock = (stockStr == null) ? 0 : Integer.parseInt(stockStr);
        if (stock > 0) {
            stock--;
            redisTemplate.opsForValue().set(STOCK, String.valueOf(stock));
            log.info("剩餘：{}", stock);
            return "成功扣除庫存，剩餘：" + stock;
        } else {
            return "庫存不足！";
        }
    }

    @Override
    public String reduceStock2() {
        log.info(redisTemplate.opsForValue().get(STOCK));

        // 這一行直接完成 讀取+修改+寫回，且中間不會被中斷
        Long currentStock = redisTemplate.opsForValue().decrement(STOCK);
        if (currentStock != null && currentStock >= 0) {
            return "成功扣除庫存，剩餘：" + currentStock;
        } else {
            redisTemplate.opsForValue().increment(STOCK);
            return "庫存不足！";
        }
    }

    @Override
    public String reduceStock3() {
        return "待補充";
    }
}
