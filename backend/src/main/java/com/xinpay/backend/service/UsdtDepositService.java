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
        if (amount == null || amount <= 0 || originalName == null || originalName.isEmpty() || file.getSize() == 0) {
            throw new IOException("Invalid input");
        }

        String extension = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : "";
        String fileName = UUID.randomUUID() + extension;
        String uploadDir = System.getProperty("user.home") + File.separator + "xinpay-uploads" + File.separator;

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();

        File destination = new File(uploadDir + fileName);
        file.transferTo(destination);

        UsdtDepositRequest deposit = new UsdtDepositRequest();
        deposit.setUserId(userId);
        deposit.setImageUrl(fileName);
        deposit.setAmount(amount);
        deposit.setVerified(false);
        deposit.setRejected(false); // ✅ set rejected = false

        return usdtDepositRequestRepository.save(deposit);
    }

    public Optional<UsdtDepositRequest> getDepositByUserId(String userId) {
        return usdtDepositRequestRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    public List<UsdtDepositRequest> getAllDepositsByUser(String userId) {
        return usdtDepositRequestRepository.findAllByUserIdOrderByIdDesc(userId);
    }

    public List<UsdtDepositRequest> getPendingDeposits() {
        return usdtDepositRequestRepository.findByVerifiedFalseAndRejectedFalse();
    }

    public boolean verifyDeposit(Long id) {
        Optional<UsdtDepositRequest> depositOpt = usdtDepositRequestRepository.findById(id);
        if (depositOpt.isPresent()) {
            UsdtDepositRequest req = depositOpt.get();
            if (!req.isVerified() && !req.isRejected()) {
                req.setVerified(true);
                req.setVerifiedAt(LocalDateTime.now());
                usdtDepositRequestRepository.save(req);

                Balance balance = balanceRepository.findById(req.getUserId()).orElseGet(() -> {
                    Balance b = new Balance();
                    b.setUserId(req.getUserId());
                    b.setInrBalance(0.0);
                    b.setUsdtBalance(0.0);
                    return b;
                });

                balance.setUsdtBalance(balance.getUsdtBalance() + req.getAmount());
                balanceRepository.save(balance);

                try {
                    Long userIdLong = Long.parseLong(req.getUserId());
                    userRepository.findById(userIdLong).ifPresent(user -> {
                        emailService.sendUsdtDepositApprovedEmail(user.getEmail(), user.getFullName(), req.getAmount());
                    });
                } catch (NumberFormatException ignored) {}
            }
            return true;
        }
        return false;
    }

    // ✅ NEW: Reject Deposit
    public boolean rejectDeposit(Long id) {
        Optional<UsdtDepositRequest> depositOpt = usdtDepositRequestRepository.findById(id);
        if (depositOpt.isPresent()) {
            UsdtDepositRequest req = depositOpt.get();
            if (!req.isVerified()) {
                req.setRejected(true);
                usdtDepositRequestRepository.save(req);
            }
            return true;
        }
        return false;
    }

    public double getTotalBalanceByUser(String userId) {
        Balance balance = balanceRepository.findById(userId).orElse(null);
        return balance != null ? balance.getUsdtBalance() : 0.0;
    }
}
