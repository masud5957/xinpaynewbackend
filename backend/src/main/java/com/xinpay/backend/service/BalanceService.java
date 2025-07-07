package com.xinpay.backend.service;

import com.xinpay.backend.model.Balance;
import com.xinpay.backend.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepo;

    // ✅ Get or create a balance record
    public Balance getOrCreateBalance(String userId) {
        return balanceRepo.findById(userId).orElseGet(() -> {
            Balance b = new Balance(userId, 0, 0);
            return balanceRepo.save(b);
        });
    }

    // ✅ Add INR
    public void addInr(String userId, double amount) {
        Balance balance = getOrCreateBalance(userId);
        balance.setInrBalance(balance.getInrBalance() + amount);
        balanceRepo.save(balance);
    }

    // ✅ Subtract INR safely
    public void subtractInr(String userId, double amount) {
        Balance balance = getOrCreateBalance(userId);
        if (balance.getInrBalance() >= amount) {
            balance.setInrBalance(balance.getInrBalance() - amount);
            balanceRepo.save(balance);
        } else {
            throw new RuntimeException("Insufficient INR balance");
        }
    }

    // ✅ Add USDT
    public void addUsdt(String userId, double amount) {
        Balance balance = getOrCreateBalance(userId);
        balance.setUsdtBalance(balance.getUsdtBalance() + amount);
        balanceRepo.save(balance);
    }

    // ✅ Subtract USDT safely
    public void subtractUsdt(String userId, double amount) {
        Balance balance = getOrCreateBalance(userId);
        if (balance.getUsdtBalance() >= amount) {
            balance.setUsdtBalance(balance.getUsdtBalance() - amount);
            balanceRepo.save(balance);
        } else {
            throw new RuntimeException("Insufficient USDT balance");
        }
    }

    // ✅ Get current INR balance
    public double getInr(String userId) {
        return getOrCreateBalance(userId).getInrBalance();
    }

    // ✅ Get current USDT balance
    public double getUsdt(String userId) {
        return getOrCreateBalance(userId).getUsdtBalance();
    }
}
