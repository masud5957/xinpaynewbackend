package com.xinpay.backend.controller;

import com.xinpay.backend.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/balance")
@CrossOrigin("*") // Allow requests from any origin
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    // ✅ Endpoint to get INR and USDT balance for a user
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("inrBalance", balanceService.getInr(userId));
        response.put("usdtBalance", balanceService.getUsdt(userId));
        return ResponseEntity.ok(response);
    }
}
