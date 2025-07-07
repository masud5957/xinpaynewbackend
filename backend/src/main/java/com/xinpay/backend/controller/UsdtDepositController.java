package com.xinpay.backend.controller;

import com.xinpay.backend.model.UsdtDepositRequest;
import com.xinpay.backend.service.UsdtDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/usdt-deposits")
@CrossOrigin(origins = "*")
public class UsdtDepositController {

    @Autowired
    private UsdtDepositService usdtDepositService;

    // ✅ Upload USDT deposit
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("userId") String userId,
            @RequestParam("amount") Double amount,
            @RequestPart("file") MultipartFile file) {
        try {
            UsdtDepositRequest saved = usdtDepositService.uploadDeposit(userId, file, amount);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    // ✅ Get latest USDT deposit status for user
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable String userId) {
        Optional<UsdtDepositRequest> deposit = usdtDepositService.getDepositByUserId(userId);
        return deposit.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Admin: Get pending USDT deposits
    @GetMapping("/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingDeposits() {
        List<UsdtDepositRequest> pending = usdtDepositService.getPendingDeposits();
        List<Map<String, Object>> result = new ArrayList<>();

        String baseUrl = "https://xinpay-backend.onrender.com"; // Replace with your deployed backend URL

        for (UsdtDepositRequest deposit : pending) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", deposit.getId());
            row.put("userId", deposit.getUserId());
            row.put("status", deposit.isVerified() ? "Verified" : "Pending");
            row.put("amount", deposit.getAmount());
            row.put("screenshotUrl", baseUrl + "/uploads/" + deposit.getImageUrl());
            result.add(row);
        }
        

        return ResponseEntity.ok(result);
    }

    // ✅ Admin: Verify USDT deposit (only needed if not using AdminDepositController)
    @PutMapping("/{id}/verify")
    public ResponseEntity<?> verify(@PathVariable Long id) {
        boolean status = usdtDepositService.verifyDeposit(id);
        return status ? ResponseEntity.ok("Verified") : ResponseEntity.status(404).body("Not found");
    }

 // ✅ User: Get all USDT deposit history (formatted with verifiedAt and type)
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getAll(@PathVariable String userId) {
        List<UsdtDepositRequest> all = usdtDepositService.getAllDepositsByUser(userId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (UsdtDepositRequest deposit : all) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", deposit.getId());
            entry.put("userId", deposit.getUserId());
            entry.put("amount", deposit.getAmount());
            entry.put("verified", deposit.isVerified());
            entry.put("type", deposit.getAmount() < 0 ? "Withdrawal" : "Deposit");

            // ✅ Format verifiedAt timestamp
            if (deposit.getVerifiedAt() != null) {
                String formattedDateTime = deposit.getVerifiedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                entry.put("verifiedAt", formattedDateTime);
            }

            response.add(entry);
        }

        return ResponseEntity.ok(response);
    }


    // ✅ User: Get USDT balance
    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getBalance(@PathVariable String userId) {
        double total = usdtDepositService.getTotalBalanceByUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("totalBalance", total);
        return ResponseEntity.ok(response);
    }
}
