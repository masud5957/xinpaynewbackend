package com.xinpay.backend.service;

import com.xinpay.backend.model.InrWithdrawRequest;
import com.xinpay.backend.repository.UserRepository;
import com.xinpay.backend.repository.InrWithdrawRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InrWithdrawService {

    @Autowired
    private InrWithdrawRequestRepository withdrawRepo;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    public InrWithdrawRequest saveWithdrawRequest(InrWithdrawRequest request) {
        request.setApproved(false);
        request.setRejected(false);
        return withdrawRepo.save(request);
    }

    public List<InrWithdrawRequest> getAllWithdrawalsByUser(String userId) {
        return withdrawRepo.findAllByUserIdOrderByIdDesc(userId);
    }

    public List<InrWithdrawRequest> getPendingWithdrawals() {
        return withdrawRepo.findByApprovedFalseAndRejectedFalse();
    }

    public boolean approveWithdrawal(Long id) {
        Optional<InrWithdrawRequest> optional = withdrawRepo.findById(id);
        if (optional.isPresent()) {
            InrWithdrawRequest request = optional.get();

            if (!request.isApproved() && !request.isRejected()) {
                double currentBalance = balanceService.getInr(request.getUserId());

                if (currentBalance >= request.getAmount()) {
                    balanceService.subtractInr(request.getUserId(), request.getAmount());

                    request.setApproved(true);
                    withdrawRepo.save(request);

                    try {
                        Long userIdLong = Long.parseLong(request.getUserId());
                        userRepository.findById(userIdLong).ifPresent(user -> {
                            emailService.sendInrWithdrawApprovedEmail(
                                    user.getEmail(),
                                    user.getFullName(),
                                    request.getAmount()
                            );
                        });
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid userId: " + request.getUserId());
                    }

                    return true;
                } else {
                    throw new RuntimeException("Insufficient balance.");
                }
            }
        }
        return false;
    }

    public boolean rejectWithdrawal(Long id) {
        Optional<InrWithdrawRequest> optional = withdrawRepo.findById(id);
        if (optional.isPresent()) {
            InrWithdrawRequest request = optional.get();

            if (!request.isApproved() && !request.isRejected()) {
                request.setRejected(true);
                withdrawRepo.save(request);
                return true;
            }
        }
        return false;
    }
}
