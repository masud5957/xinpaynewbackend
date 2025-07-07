// ✅ SERVICE: UsdtWithdrawService.java
package com.xinpay.backend.service;

import com.xinpay.backend.model.UsdtWithdrawRequest;
import com.xinpay.backend.repository.UsdtWithdrawRequestRepository;
import com.xinpay.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsdtWithdrawService {

    @Autowired
    private UsdtWithdrawRequestRepository withdrawRepo;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public UsdtWithdrawRequest saveWithdrawRequest(UsdtWithdrawRequest request) {
        request.setApproved(false);
        request.setRejected(false);
        return withdrawRepo.save(request);
    }

    public List<UsdtWithdrawRequest> getAllWithdrawalsByUser(String userId) {
        return withdrawRepo.findAllByUserIdOrderByIdDesc(userId);
    }

    public List<UsdtWithdrawRequest> getPendingWithdrawals() {
        return withdrawRepo.findByApprovedFalseAndRejectedFalse();
    }

    public boolean approveWithdrawal(Long id) {
        Optional<UsdtWithdrawRequest> optional = withdrawRepo.findById(id);
        if (optional.isPresent()) {
            UsdtWithdrawRequest request = optional.get();
            double currentBalance = balanceService.getUsdt(request.getUserId());
            if (currentBalance >= request.getAmount()) {
                balanceService.subtractUsdt(request.getUserId(), request.getAmount());
                request.setApproved(true);
                withdrawRepo.save(request);
                return true;
            } else {
                throw new RuntimeException("Insufficient USDT balance.");
            }
        }
        return false;
    }

    public boolean rejectWithdrawal(Long id) {
        Optional<UsdtWithdrawRequest> optional = withdrawRepo.findById(id);
        if (optional.isPresent()) {
            UsdtWithdrawRequest request = optional.get();
            request.setRejected(true);
            withdrawRepo.save(request);
            return true;
        }
        return false;
    }
}
