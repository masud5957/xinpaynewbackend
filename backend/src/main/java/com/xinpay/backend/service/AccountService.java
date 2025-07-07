package com.xinpay.backend.service;

import com.xinpay.backend.model.Account;
import com.xinpay.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Save a new account
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    // Get all accounts for a user
    public List<Account> getAccountsByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

 // ✅ Delete a specific account by ID (Long type)
    public void deleteAccountById(Long accountId) {
        accountRepository.deleteById(accountId);
    }
}
