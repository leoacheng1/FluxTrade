package com.fluxtrade.engine.service;

import com.fluxtrade.engine.model.PriceRecord;
import com.fluxtrade.engine.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // 自動注入 Kafka 工具

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BINANCE_API = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";

    /**
     * 撈取比特幣價格function
     */
//    @Scheduled(fixedRate = 5000)
    public void fetchPrice() {
        try {
            Map<String, String> response = restTemplate.getForObject(BINANCE_API, Map.class);
            if (response != null) {
                String priceStr = response.get("price");

                // 1. 存入 MySQL (長期紀錄)
                PriceRecord record = new PriceRecord();
                record.setSymbol("BTCUSDT");
                record.setPrice(Double.parseDouble(priceStr));
                record.setTimestamp(LocalDateTime.now());
                priceRepository.save(record);

                // 2. 存入 Redis (即時快取)
                redisTemplate.opsForValue().set("BTC_LATEST", priceStr, 60, TimeUnit.SECONDS);

                // 3. 發送到 Kafka Topic (事件廣播)
                // 我們把這個主題取名為 "coin-price"
                // 修改後的發送邏輯
                var future = kafkaTemplate.send("coin-price", "BTCUSDT", priceStr);

                future.whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("✅ [傳送成功] 偏移量(Offset): " + result.getRecordMetadata().offset());
                    } else {
                        System.err.println("❌ [傳送失敗] 原因: " + ex.getMessage());
                    }
                });

                System.out.println("📢 [Kafka 廣播] 主題: coin-price | 幣種: BTCUSDT | 價格: " + priceStr);
            }
        } catch (Exception e) {
            System.err.println("❌ 處理失敗: " + e.getMessage());
        }
    }
}