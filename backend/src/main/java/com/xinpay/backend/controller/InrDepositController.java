// ✅ CONTROLLER
package com.xinpay.backend.controller;

import com.xinpay.backend.model.InrDepositRequest;
import com.xinpay.backend.service.InrDepositService;
import com.xinpay.backend.service.UsdtDepositService;
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
    private UsdtDepositService usdtDepositService;

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

    @GetMapping("/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingDeposits() {
        List<InrDepositRequest> pending = inrDepositService.getPendingDeposits();
        List<Map<String, Object>> result = new ArrayList<>();
        String baseUrl = "https://xinpay-backend.onrender.com";
        for (InrDepositRequest deposit : pending) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", deposit.getId());
            row.put("userId", deposit.getUserId());
            row.put("status", deposit.isVerified() ? "Verified" : deposit.isRejected() ? "Rejected" : "Pending");
            row.put("amount", deposit.getAmount());
            row.put("type", deposit.getAmount() < 0 ? "Withdrawal" : "Deposit");
            row.put("screenshotUrl", baseUrl + "/uploads/" + deposit.getImageUrl());
            result.add(row);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<?> verify(@PathVariable Long id) {
        boolean status = inrDepositService.verifyDeposit(id);
        return status ? ResponseEntity.ok("Verified") : ResponseEntity.status(404).body("Not found");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        boolean status = inrDepositService.rejectDeposit(id);
        return status ? ResponseEntity.ok("Rejected") : ResponseEntity.status(404).body("Not found");
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getStatus(@PathVariable String userId) {
        Optional<InrDepositRequest> deposit = inrDepositService.getDepositByUserId(userId);
        return deposit.<ResponseEntity<?>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

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
            entry.put("rejected", deposit.isRejected());
            entry.put("type", deposit.getAmount() < 0 ? "Withdrawal" : "Deposit");
            if (deposit.getVerifiedAt() != null) {
                String formatted = deposit.getVerifiedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                entry.put("verifiedAt", formatted);
            }
            response.add(entry);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance/combined/{userId}")
    public ResponseEntity<?> getCombinedBalance(@PathVariable String userId) {
        double inrBalance = inrDepositService.getTotalBalanceByUser(userId);
        double usdtBalance = usdtDepositService.getTotalBalanceByUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("inrBalance", inrBalance);
        response.put("usdtBalance", usdtBalance);
        return ResponseEntity.ok(response);
    }
}
