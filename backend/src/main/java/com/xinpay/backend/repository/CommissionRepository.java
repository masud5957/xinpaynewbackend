package com.xinpay.backend.repository;

import com.xinpay.backend.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findByUserId(String userId);
}
