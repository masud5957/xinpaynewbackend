package com.xinpay.backend.controller;

import com.xinpay.backend.model.Commission;
import com.xinpay.backend.service.CommissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissions")
@CrossOrigin
public class CommissionController {

    @Autowired
    private CommissionService commissionService;

    // 👉 Submit a new commission
    @PostMapping
    public Commission addCommission(@RequestBody Commission commission) {
        return commissionService.saveCommission(commission);
    }

    // 👉 Get all commissions by userId
    @GetMapping("/user/{userId}")
    public List<Commission> getCommissionsByUserId(@PathVariable String userId) {
        return commissionService.getCommissionsByUserId(userId);
    }

    // ✅ Delete commission by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Long id) {
        commissionService.deleteCommission(id);
        return ResponseEntity.ok().build();
    }
}
