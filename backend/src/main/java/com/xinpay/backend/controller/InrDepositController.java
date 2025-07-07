package com.xinpay.backend.controller;

import com.xinpay.backend.model.InrDepositRequest;
import com.xinpay.backend.service.InrDepositService;
import com.xinpay.backend.service.UsdtDepositService; // ✅ Import USDT service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/inr-deposits")
@CrossOrigin(origins = "*")
public class InrDepositController {

    @Autowired
    private InrDepositService inrDepositService;

    @Autowired
    private UsdtDepositService usdtDepositService; // ✅ Inject USDT service

    // ✅ Upload INR deposit
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("userId") String userId,
            @RequestParam("amount") Double amount,
            @RequestPart("file") MultipartFile file) {
        try {
            InrDepositRequest saved = inrDepositService.uploadDeposit(userId, file, amount);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    // ✅ Get latest deposit status for user
    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable String userId) {
        Optional<InrDepositRequest> deposit = inrDepositService.getDepositByUserId(userId);
        return deposit.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Admin: Get pending INR deposits
    @GetMapping("/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingDeposits() {
        List<InrDepositRequest> pending = inrDepositService.getPendingDeposits();
        List<Map<String, Object>> result = new ArrayList<>();

        String baseUrl = "https://xinpay-backend.onrender.com";

        for (InrDepositRequest deposit : pending) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", deposit.getId());
            row.put("userId", deposit.getUserId());
            row.put("status", deposit.isVerified() ? "Verified" : "Pending");
            row.put("amount", deposit.getAmount());
            row.put("type", deposit.getAmount() < 0 ? "Withdrawal" : "Deposit");
            row.put("screenshotUrl", baseUrl + "/uploads/" + deposit.getImageUrl());
            result.add(row);
        }

        return ResponseEntity.ok(result);
    }

    // ✅ Admin: Verify deposit
    @PutMapping("/{id}/verify")
    public ResponseEntity<?> verify(@PathVariable Long id) {
        boolean status = inrDepositService.verifyDeposit(id);
        return status ? ResponseEntity.ok("Verified") : ResponseEntity.status(404).body("Not found");
    }

 // // ✅ User: Get all deposit history
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getAll(@PathVariable String userId) {
        List<InrDepositRequest> all = inrDepositService.getAllDepositsByUser(userId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (InrDepositRequest deposit : all) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", deposit.getId());
            entry.put("userId", deposit.getUserId());
            entry.put("amount", deposit.getAmount());
            entry.put("verified", deposit.isVerified());
            entry.put("type", deposit.getAmount() < 0 ? "Withdrawal" : "Deposit");

            // ✅ Format verifiedAt using a readable date-time format
            if (deposit.getVerifiedAt() != null) {
                String formattedDateTime = deposit.getVerifiedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                entry.put("verifiedAt", formattedDateTime);
            }

            response.add(entry);
        }

        return ResponseEntity.ok(response);
    }


    
    

    // ✅ User: Get current INR + USDT balance
    @GetMapping("/balance/combined/{userId}")
    public ResponseEntity<?> getCombinedBalance(@PathVariable String userId) {
        double inrBalance = inrDepositService.getTotalBalanceByUser(userId);
        double usdtBalance = usdtDepositService.getTotalBalanceByUser(userId); // ✅ from USDT service

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("inrBalance", inrBalance);
        response.put("usdtBalance", usdtBalance);

        return ResponseEntity.ok(response);
    }
}