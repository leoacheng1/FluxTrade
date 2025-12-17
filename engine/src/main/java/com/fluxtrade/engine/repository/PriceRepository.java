package com.fluxtrade.engine.repository;

import com.fluxtrade.engine.model.PriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository 已經內建了 save(), findAll(), delete() 等功能
public interface PriceRepository extends JpaRepository<PriceRecord, Long> {
}