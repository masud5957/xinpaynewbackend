package com.xinpay.backend.controller;

import com.xinpay.backend.service.UsdtWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private UsdtWalletService walletService;

    // 🔐 Secret admin key for protecting the update endpoint
    private static final String ADMIN_KEY = "xinpay@786";

    @GetMapping("/usdt-address")
    public ResponseEntity<String> getUsdtAddress() {
        String address = walletService.getLatestTrc20Address();
        if (address != null) {
            return ResponseEntity.ok(address);
        } else {
            return ResponseEntity.status(404).body("TRC20 address not found");
        }
    }

    @PutMapping("/usdt-address")
    public ResponseEntity<String> updateUsdtAddress(
            @RequestHeader(value = "X-ADMIN-KEY", required = false) String adminKey,
            @RequestBody String newAddress) {

        if (adminKey == null || !adminKey.equals(ADMIN_KEY)) {
            return ResponseEntity.status(403).body("❌ Unauthorized: Invalid admin key");
        }

        String msg = walletService.updateTrc20Address(newAddress);
        return ResponseEntity.ok(msg);
    }
}
