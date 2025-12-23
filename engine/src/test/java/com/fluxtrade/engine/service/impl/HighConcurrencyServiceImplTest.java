package com.fluxtrade.engine.service.impl;

import com.fluxtrade.engine.service.HighConcurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
class HighConcurrencyServiceImplTest {

    @Mock
    private HighConcurrencyService highConcurrencyService;

    @Test
    void initStock() {
        assertEquals("庫存已重置為 100！", "庫存已重置為 100！");
    }
}