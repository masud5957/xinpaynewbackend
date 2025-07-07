// 2. BalanceRepository.java
package com.xinpay.backend.repository;

import com.xinpay.backend.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, String> {
    // userId is primary key
}