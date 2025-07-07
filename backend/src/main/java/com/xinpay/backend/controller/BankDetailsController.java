package com.xinpay.backend.controller;

import com.xinpay.backend.model.BankDetails;
import com.xinpay.backend.service.BankDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank-details")
@CrossOrigin(origins = "*")
public class BankDetailsController {

    private final BankDetailsService service;

    public BankDetailsController(BankDetailsService service) {
        this.service = service;
    }

    // ✅ GET current bank details
    @GetMapping
    public ResponseEntity<BankDetails> getBankDetails() {
        BankDetails details = service.getBankDetails();
        return (details == null) ? ResponseEntity.noContent().build() : ResponseEntity.ok(details);
    }

    // ✅ Update via mobile/JSON body
    @PostMapping("/update")
    public ResponseEntity<BankDetails> updateBankDetails(@RequestBody BankDetails details) {
        if (details.getAccountNumber() == null || details.getIfscCode() == null || details.getAccountHolder() == null) {
            return ResponseEntity.badRequest().build();
        }
        BankDetails updated = service.updateBankDetails(details);
        return ResponseEntity.ok(updated);
    }

    // ✅ Admin Panel upload using provided direct ImgBB URL
    @PostMapping("/admin/update")
    public ResponseEntity<BankDetails> updateBankDetailsWithQr(
            @RequestParam String accountNumber,
            @RequestParam String ifscCode,
            @RequestParam String accountHolder,
            @RequestParam String qrUrl // ImgBB direct link input from admin panel
    ) {
        try {
            BankDetails newDetails = new BankDetails(accountNumber, ifscCode, accountHolder, qrUrl);
            BankDetails updated = service.updateBankDetails(newDetails);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
