package com.fluxtrade.engine.service.impl;


import com.fluxtrade.engine.service.HighConcurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static java.util.Collections.singletonList;

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
        // 1. 編寫 Lua 腳本
        // KEYS[1] 代表 Redis 的 key ("stock")
        // ARGV[1] 代表要扣減的數量 (通常是 "1")
        String script = "local stock = redis.call('get', KEYS[1]); " +
                        "if (stock == nil) then return -1; end; " + // Key 不存在
                        "stock = tonumber(stock); " +
                        "if (stock > 0) then " +
                        "    return redis.call('decr', KEYS[1]); " + // 還有庫存就扣減
                        "else " +
                        "    return -2; " + // 庫存不足
                        "end;";

        // 2. 執行腳本
        // execute(腳本物件, keys 列表, args 列表)
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), singletonList(STOCK), "1");

        // 3. 根據回傳值判斷結果
        if (result != null && result >= 0) {
            return "【Lua 成功】剩餘庫存：" + result;
        } else if (result != null && result == -1) {
            return "系統錯誤：庫存 Key 不存在！";
        } else {
            return "【Lua 失敗】庫存不足！";
        }
    }
}
