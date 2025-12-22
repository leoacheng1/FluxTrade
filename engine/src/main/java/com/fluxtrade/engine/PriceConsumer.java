package com.fluxtrade.engine; // 👈 確保這行與你的啟動類 FluxTradeApplication 一致

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service // 👈 沒這行，Spring 就會當這個類別不存在
public class PriceConsumer {

    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("✅ [系統檢查] PriceConsumer 已經成功被 Spring 載入了！");
    }

    // 1. groupId 換一個全新的，保證從頭讀起
    // 2. 暫時不要寫複雜邏輯，只印出一行字
    @KafkaListener(topics = "coin-price", groupId = "mega-debug-group-444") // 改成這個
    public void listen(String message) {
        System.out.println("🎧 [終於抓到了] 收到來自 Kafka 的訊息: " + message);
    }
}