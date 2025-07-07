package com.xinpay.backend.repository;

import com.xinpay.backend.model.InrWithdrawRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InrWithdrawRequestRepository extends JpaRepository<InrWithdrawRequest, Long> {

    List<InrWithdrawRequest> findByApprovedFalse();

    List<InrWithdrawRequest> findAllByUserIdOrderByIdDesc(String userId);
}