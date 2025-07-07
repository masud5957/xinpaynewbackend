package com.xinpay.backend.service;


import com.xinpay.backend.model.Balance;
import com.xinpay.backend.model.InrDepositRequest;
import com.xinpay.backend.repository.BalanceRepository;
import com.xinpay.backend.repository.InrDepositRequestRepository;
import com.xinpay.backend.repository.UserRepository;

import com.xinpay.backend.model.UsdtWithdrawRequest;
import com.xinpay.backend.repository.UsdtWithdrawRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessagingException;

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
    
    @Autowired
    private NotificationService notificationService;
    
    public UsdtWithdrawRequest saveWithdrawRequest(UsdtWithdrawRequest request) {
        request.setApproved(false);
        return withdrawRepo.save(request);
    }

    public List<UsdtWithdrawRequest> getAllWithdrawalsByUser(String userId) {
        return withdrawRepo.findAllByUserIdOrderByIdDesc(userId);
    }

    public List<UsdtWithdrawRequest> getPendingWithdrawals() {
        return withdrawRepo.findByApprovedFalse();
    }

    public boolean approveWithdrawal(Long id) {
        Optional<UsdtWithdrawRequest> optional = withdrawRepo.findById(id);

        if (optional.isPresent()) {
            UsdtWithdrawRequest request = optional.get();
            double currentBalance = balanceService.getUsdt(request.getUserId());

            if (currentBalance >= request.getAmount()) {
                // 💰 Subtract balance
                balanceService.subtractUsdt(request.getUserId(), request.getAmount());

                // ✅ Approve request
                request.setApproved(true);
                withdrawRepo.save(request);

                // 📩 Email + 🔔 FCM Notification
                try {
                    Long userIdLong = Long.parseLong(request.getUserId());
                    userRepository.findById(userIdLong).ifPresent(user -> {
                        // ✉️ Email
                        emailService.sendUsdtWithdrawApprovedEmail(
                                user.getEmail(),
                                user.getFullName(),
                                request.getAmount()
                        );

                        // 🔔 Push Notification
                        if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                            try {
                                notificationService.sendUsdtWithdrawApproved(user.getFcmToken(), request.getAmount());
                            } catch (FirebaseMessagingException e) {
                                System.err.println("❌ FCM failed for USDT withdrawal: " + e.getMessage());
                            }
                        }
                    });
                } catch (NumberFormatException e) {
                    System.err.println("❌ Invalid userId format in USDT withdrawal: " + request.getUserId());
                }

                return true;
            } else {
                throw new RuntimeException("Insufficient USDT balance.");
            }
        }

        return false;
    }


}
