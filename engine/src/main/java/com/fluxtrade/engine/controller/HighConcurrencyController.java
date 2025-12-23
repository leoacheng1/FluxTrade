package com.fluxtrade.engine.controller;

import com.fluxtrade.engine.service.HighConcurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HighConcurrencyController {

    private final HighConcurrencyService highConcurrencyService;

    @GetMapping("/init")
    public String initStock() {
        return highConcurrencyService.initStock();
    }

    @GetMapping("/reduce1")
    public String reduceStock1() {
        return highConcurrencyService.reduceStock1();
    }

    @GetMapping("/reduce2")
    public String reduceStock2() {
        return highConcurrencyService.reduceStock2();
    }

    @GetMapping("/reduce3")
    public String reduceStock3() {
        return highConcurrencyService.reduceStock3();
    }
}
