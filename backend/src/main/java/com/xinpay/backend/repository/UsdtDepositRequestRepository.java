package com.xinpay.backend.repository;

import com.xinpay.backend.model.UsdtDepositRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsdtDepositRequestRepository extends JpaRepository<UsdtDepositRequest, Long> {

    Optional<UsdtDepositRequest> findTopByUserIdOrderByIdDesc(String userId);

    List<UsdtDepositRequest> findAllByUserIdOrderByIdDesc(String userId);

    List<UsdtDepositRequest> findByVerifiedFalseAndRejectedFalse(); // ✅ Only show pending + not rejected
}
