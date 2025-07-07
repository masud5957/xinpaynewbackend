package com.xinpay.backend.service;

import com.xinpay.backend.model.Balance;

import com.xinpay.backend.model.UsdtDepositRequest;
import com.xinpay.backend.repository.BalanceRepository;
import com.xinpay.backend.repository.UsdtDepositRequestRepository;
import com.xinpay.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import com.google.firebase.messaging.FirebaseMessagingException;

@Service
public class UsdtDepositService {

    @Autowired
    private UsdtDepositRequestRepository usdtDepositRequestRepository;

    @Autowired
    private BalanceRepository balanceRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private NotificationService notificationService;

    public UsdtDepositRequest uploadDeposit(String userId, MultipartFile file, Double amount) throws IOException {
        String originalName = file.getOriginalFilename();
        long size = file.getSize();
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }


        if (originalName == null || originalName.isEmpty() || size == 0) {
            throw new IOException("Invalid file. Name or size is missing.");
        }

        String extension = "";
        if (originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        String fileName = UUID.randomUUID() + extension;
        String uploadDir = System.getProperty("user.home") + File.separator + "xinpay-uploads" + File.separator;
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();

        File destination = new File(uploadDir + fileName);
        file.transferTo(destination);

        UsdtDepositRequest deposit = new UsdtDepositRequest();
        deposit.setUserId(userId);
        deposit.setImageUrl(fileName);
        deposit.setVerified(false);
        deposit.setAmount(amount);
        

        return usdtDepositRequestRepository.save(deposit);
    }

    public Optional<UsdtDepositRequest> getDepositByUserId(String userId) {
        return usdtDepositRequestRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    public List<UsdtDepositRequest> getAllDepositsByUser(String userId) {
        return usdtDepositRequestRepository.findAllByUserIdOrderByIdDesc(userId);
    }

    public boolean verifyDeposit(Long id) {
        Optional<UsdtDepositRequest> depositOpt = usdtDepositRequestRepository.findById(id);
        if (depositOpt.isPresent()) {
            UsdtDepositRequest req = depositOpt.get();

            if (!req.isVerified()) {
                req.setVerified(true);
                req.setVerifiedAt(java.time.LocalDateTime.now());
                usdtDepositRequestRepository.save(req);

                // 🔁 Update user's USDT balance
                Balance balance = balanceRepository.findById(req.getUserId())
                        .orElseGet(() -> {
                            Balance newBalance = new Balance();
                            newBalance.setUserId(req.getUserId());
                            newBalance.setInrBalance(0.0);
                            newBalance.setUsdtBalance(0.0);
                            return newBalance;
                        });

                balance.setUsdtBalance(balance.getUsdtBalance() + req.getAmount());
                balanceRepository.save(balance);

                // ✅ Send confirmation email 
                try {
                    Long userIdLong = Long.parseLong(req.getUserId());
                    userRepository.findById(userIdLong).ifPresent(user -> {
                        // ✉️ Email
                        emailService.sendUsdtDepositApprovedEmail(
                                user.getEmail(),
                                user.getFullName(),
                                req.getAmount()
                        );

                    });
                } catch (NumberFormatException e) {
                    System.err.println("❌ Invalid userId format for USDT deposit: " + req.getUserId());
                }
            }

            return true;
        }

        return false;
    }



    public List<UsdtDepositRequest> getPendingDeposits() {
        return usdtDepositRequestRepository.findByVerifiedFalse();
    }

    public double getTotalBalanceByUser(String userId) {
        Balance balance = balanceRepository.findById(userId).orElse(null);
        return balance != null ? balance.getUsdtBalance() : 0.0;
    }
}
