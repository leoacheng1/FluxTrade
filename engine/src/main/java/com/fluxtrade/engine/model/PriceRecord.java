package com.fluxtrade.engine.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity // 告訴 Hibernate：這是一個資料表實體
@Table(name = "price_records") // 指定資料表名稱
@Data
public class PriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動跳號 (Auto Increment)
    private Long id;

    private String symbol; // 幣種，例如 BTCUSDT

    private Double price;  // 價格

    private LocalDateTime timestamp; // 抓取時間
}