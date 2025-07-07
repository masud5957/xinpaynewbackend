// 1. Create Balance.java entity
package com.xinpay.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "balances")
public class Balance {

    @Id
    private String userId;

    private double inrBalance;
    private double usdtBalance;

    public Balance() {}

    public Balance(String userId, double inrBalance, double usdtBalance) {
        this.userId = userId;
        this.inrBalance = inrBalance;
        this.usdtBalance = usdtBalance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getInrBalance() {
        return inrBalance;
    }

    public void setInrBalance(double inrBalance) {
        this.inrBalance = inrBalance;
    }

    public double getUsdtBalance() {
        return usdtBalance;
    }

    public void setUsdtBalance(double usdtBalance) {
        this.usdtBalance = usdtBalance;
    }
} 
