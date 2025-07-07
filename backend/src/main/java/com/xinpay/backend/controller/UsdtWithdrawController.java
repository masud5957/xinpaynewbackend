package com.xinpay.backend.controller;

import com.xinpay.backend.model.UsdtWithdrawRequest;
import com.xinpay.backend.service.UsdtWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/usdt-withdraw")
@CrossOrigin(origins = "*")
public class UsdtWithdrawController {

    @Autowired
    private UsdtWithdrawService withdrawService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    // ✅ User submits USDT withdraw request
    @PostMapping("/request")
    public ResponseEntity<UsdtWithdrawRequest> requestWithdraw(@RequestBody UsdtWithdrawRequest request) {
        UsdtWithdrawRequest savedRequest = withdrawService.saveWithdrawRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    // ✅ Admin fetches all pending USDT withdrawals
    @GetMapping("/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingWithdrawals() {
        List<UsdtWithdrawRequest> pendingList = withdrawService.getPendingWithdrawals();
        List<Map<String, Object>> response = new ArrayList<>();

        for (UsdtWithdrawRequest req : pendingList) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", req.getId());
            entry.put("userId", req.getUserId());
            entry.put("amount", req.getAmount());
            entry.put("walletAddress", req.getWalletAddress());
            entry.put("approved", req.isApproved());
            if (req.getRequestedAt() != null) {
                entry.put("requestedAt", formatter.format(req.getRequestedAt()));
            }
            response.add(entry);
        }

        return ResponseEntity.ok(response);
    }

    // ✅ Admin approves a USDT withdrawal
    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveWithdraw(@PathVariable Long id) {
        boolean approved = withdrawService.approveWithdrawal(id);
        if (approved) {
            return ResponseEntity.ok("✅ USDT Withdrawal approved successfully.");
        } else {
            return ResponseEntity.status(404).body("❌ Request not found or insufficient USDT balance.");
        }
    }

    // ✅ User sees all their withdrawal history
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getAllByUser(@PathVariable String userId) {
        List<UsdtWithdrawRequest> history = withdrawService.getAllWithdrawalsByUser(userId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (UsdtWithdrawRequest entry : history) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", entry.getId());
            row.put("userId", entry.getUserId());
            row.put("amount", entry.getAmount());
            row.put("walletAddress", entry.getWalletAddress());
            row.put("approved", entry.isApproved());
            if (entry.getRequestedAt() != null) {
                row.put("requestedAt", formatter.format(entry.getRequestedAt()));
            }
            response.add(row);
        }

        return ResponseEntity.ok(response);
    }
}
