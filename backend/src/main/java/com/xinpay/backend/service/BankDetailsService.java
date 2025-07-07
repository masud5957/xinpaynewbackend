package com.xinpay.backend.service;

import com.xinpay.backend.model.BankDetails;
import com.xinpay.backend.repository.BankDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankDetailsService {

    private final BankDetailsRepository repository;

    public BankDetailsService(BankDetailsRepository repository) {
        this.repository = repository;
    }

    public BankDetails getBankDetails() {
        return repository.findAll()
                .stream()
                .reduce((first, second) -> second) // get the last one if multiple exist
                .orElse(null);
    }

    public BankDetails updateBankDetails(BankDetails newDetails) {
        List<BankDetails> existingList = repository.findAll();

        BankDetails bankDetails;
        if (existingList.isEmpty()) {
            bankDetails = new BankDetails();
        } else {
            bankDetails = existingList.get(0);  // update existing one
        }

        bankDetails.setAccountNumber(newDetails.getAccountNumber());
        bankDetails.setIfscCode(newDetails.getIfscCode());
        bankDetails.setAccountHolder(newDetails.getAccountHolder());

        // Only update QR if new value provided
        if (newDetails.getQrImageUrl() != null) {
            bankDetails.setQrImageUrl(newDetails.getQrImageUrl());
        }

        return repository.save(bankDetails);
    }
}
