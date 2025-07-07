package com.xinpay.backend.model;

import jakarta.persistence.*;

@Entity
public class BankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String ifscCode;
    private String accountHolder;
    private String qrImageUrl;

    public BankDetails() {}

    public BankDetails(String accountNumber, String ifscCode, String accountHolder, String qrImageUrl) {
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.accountHolder = accountHolder;
        this.qrImageUrl = qrImageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getQrImageUrl() {
        return qrImageUrl;
    }

    public void setQrImageUrl(String qrImageUrl) {
        this.qrImageUrl = qrImageUrl;
    }
}
