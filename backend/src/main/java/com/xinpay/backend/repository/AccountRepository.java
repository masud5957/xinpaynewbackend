package com.xinpay.backend.repository;

import com.xinpay.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // Get all accounts for a specific user
    List<Account> findByUserId(String userId);

    // Optional override (not needed since JpaRepository already provides it)
    void deleteById(Long id);
}
