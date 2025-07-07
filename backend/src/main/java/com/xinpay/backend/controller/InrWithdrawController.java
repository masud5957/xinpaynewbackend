package com.xinpay.backend.controller;

import com.xinpay.backend.model.InrWithdrawRequest;
import com.xinpay.backend.service.InrWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/inr-withdraw")
@CrossOrigin(origins = "*")
public class InrWithdrawController {

    @Autowired
    private InrWithdrawService withdrawService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    @PostMapping("/request")
    public ResponseEntity<InrWithdrawRequest> requestWithdraw(@RequestBody InrWithdrawRequest request) {
        InrWithdrawRequest savedRequest = withdrawService.saveWithdrawRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Map<String, Object>>> getPendingWithdrawals() {
        List<InrWithdrawRequest> pendingList = withdrawService.getPendingWithdrawals();
        List<Map<String, Object>> response = new ArrayList<>();

        for (InrWithdrawRequest req : pendingList) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", req.getId());
            entry.put("userId", req.getUserId());
            entry.put("amount", req.getAmount());
            entry.put("accountNumber", req.getAccountNumber());
            entry.put("ifscCode", req.getIfscCode());
            entry.put("approved", req.isApproved());
            entry.put("rejected", req.isRejected());
            if (req.getRequestedAt() != null) {
                entry.put("requestedAt", formatter.format(req.getRequestedAt()));
            }
            response.add(entry);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveWithdraw(@PathVariable Long id) {
        boolean approved = withdrawService.approveWithdrawal(id);
        return approved
                ? ResponseEntity.ok("✅ Withdrawal approved.")
                : ResponseEntity.status(404).body("❌ Request not found or balance insufficient.");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectWithdraw(@PathVariable Long id) {
        boolean rejected = withdrawService.rejectWithdrawal(id);
        return rejected
                ? ResponseEntity.ok("❌ Withdrawal rejected.")
                : ResponseEntity.status(404).body("❌ Request not found or already processed.");
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getAllByUser(@PathVariable String userId) {
        List<InrWithdrawRequest> history = withdrawService.getAllWithdrawalsByUser(userId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (InrWithdrawRequest entry : history) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", entry.getId());
            row.put("userId", entry.getUserId());
            row.put("amount", entry.getAmount());
            row.put("accountNumber", entry.getAccountNumber());
            row.put("ifscCode", entry.getIfscCode());
            row.put("approved", entry.isApproved());
            row.put("rejected", entry.isRejected());
            if (entry.getRequestedAt() != null) {
                row.put("requestedAt", formatter.format(entry.getRequestedAt()));
            }
            response.add(row);
        }

        return ResponseEntity.ok(response);
    }
}
