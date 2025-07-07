// ✅ REPOSITORY: UsdtWithdrawRequestRepository.java
package com.xinpay.backend.repository;

import com.xinpay.backend.model.UsdtWithdrawRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsdtWithdrawRequestRepository extends JpaRepository<UsdtWithdrawRequest, Long> {
    List<UsdtWithdrawRequest> findByApprovedFalseAndRejectedFalse();
    List<UsdtWithdrawRequest> findAllByUserIdOrderByIdDesc(String userId);
}