package com.fluxtrade.engine.service.impl;


import com.fluxtrade.engine.service.HighConcurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HighConcurrencyServiceImpl implements HighConcurrencyService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public String initStock() {
        redisTemplate.opsForValue().set("stock", "100");
        return "庫存已重置為 100！";
    }

    public String reduceStock1() {
        String stockStr = redisTemplate.opsForValue().get("stock");
        System.out.println(stockStr);
        int stock = (stockStr == null) ? 0 : Integer.parseInt(stockStr);
        if (stock > 0) {
            // 故意製造一點點延遲，模擬資料庫處理時間，這會放大高併發的問題
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return e.getMessage();
            }

            stock--;
            redisTemplate.opsForValue().set("stock", String.valueOf(stock));
            System.out.println("剩餘：" + stock);
            return "成功扣除庫存，剩餘：" + stock;
        } else {
            return "庫存不足！";
        }
    }

    @Override
    public String reduceStock2() {
        System.out.println(redisTemplate.opsForValue().get("stock"));

        // 這一行直接完成 讀取+修改+寫回，且中間不會被中斷
        Long currentStock = redisTemplate.opsForValue().decrement("stock");
        if (currentStock != null && currentStock >= 0) {
            return "成功扣除庫存，剩餘：" + currentStock;
        } else {
            redisTemplate.opsForValue().increment("stock");
            return "庫存不足！";
        }
    }
}
