package com.xinpay.backend.repository;

import com.xinpay.backend.model.UsdtWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsdtWalletRepository extends JpaRepository<UsdtWallet, Long> {
    UsdtWallet findTopByOrderByIdDesc();
}
