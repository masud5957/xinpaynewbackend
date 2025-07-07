package com.xinpay.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String ifsc;

    
    @Column(nullable = false)
    private String loginDetails;

    @Column(nullable = false)
    private String fundType; // "Gaming" or "Stock"

    public Account() {
    }

    public Account(String userId, String accountType, String bankName, String accountNumber,
                   String ifsc, String loginDetails, String fundType) {
        this.userId = userId;
        this.accountType = accountType;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.ifsc = ifsc;
        this.loginDetails = loginDetails;
        this.fundType = fundType;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getLoginDetails() {
        return loginDetails;
    }

    public String getFundType() {
        return fundType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public void setLoginDetails(String loginDetails) {
        this.loginDetails = loginDetails;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }
}
