package com.fluxtrade.engine.controller;

import com.fluxtrade.engine.service.HighConcurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HighConcurrencyController {

    @Autowired
    private HighConcurrencyService highConcurrencyService;

    @GetMapping("/reduce")
    public String reduceStock() {
        return highConcurrencyService.reduceStock2();

    }

    @GetMapping("/init")
    public String initStock() {
        return highConcurrencyService.initStock();
    }
}
