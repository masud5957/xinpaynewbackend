// File: com.xinpay.backend.service.EmailService.java
package com.xinpay.backend.service;
import com.xinpay.backend.model.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessagingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private NotificationService notificationService;

    public void sendOtpEmail(String toEmail, String otp) {
        String subject = "XinPay - OTP Verification Code";
        String body = "Dear User,\n\n"
                + "Your One-Time Password (OTP) for XinPay is: " + otp + "\n\n"
                + "⚠️ Please do not share this OTP with anyone.\n"
                + "It is valid for a limited time and is required to complete your verification.\n\n"
                + "If you did not request this OTP, please contact our support team immediately.\n\n"
                + "Thank you,\n"
                + "Team XinPay";
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@xinpay.co.in");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    public void sendWelcomeEmail(String toEmail, String fullName, String agentId) {
        String subject = "Welcome to XinPay 🎉";
        String body = "Dear " + fullName + ",\n\n"
                + "Welcome to XinPay! Your account has been successfully activated.\n\n"
                + "🎯 Your created Agent ID: " + agentId + " is currently **inactive**.\n"
                + "👉 Please make a deposit through the XinPay app to activate your account and start your work smoothly.\n\n"
                + "⚠️ *Important:* Never make a deposit through any platform or method other than our official XinPay app.\n\n"
                + "We're excited to have you on board. If you have any questions, feel free to reach out to us.\n\n"
                + "Thank you,\n"
                + "Team XinPay";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@xinpay.co.in");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    
    public void sendInrDepositApprovedEmail(String toEmail, String fullName, double amount) {
        String subject = "XinPay - INR Deposit Successful ✅";
        String body = "Dear " + fullName + ",\n\n"
                + "🎉 Your INR deposit of ₹" + amount + " has been successfully verified and added to your wallet.\n\n"
                
                + "If you have any questions, feel free to contact our support.\n\n"
                + "Thank you,\n"
                + "Team XinPay";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@xinpay.co.in");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    public void sendUsdtDepositApprovedEmail(String toEmail, String fullName, double amount) {
        String subject = "XinPay - USDT Deposit Successful ✅";
        String body = "Dear " + fullName + ",\n\n"
                + "🎉 Your USDT deposit of $" + amount + " has been successfully verified and added to your wallet.\n\n"
                
                + "If you have any questions, feel free to contact our support.\n\n"
                + "Thank you,\n"
                + "Team XinPay";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@xinpay.co.in");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    public void sendUsdtWithdrawApprovedEmail(String toEmail, String fullName, double amount) {
        String subject = "XinPay - USDT Withdrawal processed ✅";
        String body = "Dear " + fullName + ",\n\n"
                + "✅ Your USDT withdrawal of $" + amount + " has been successfully approved and processed.\n\n"
                + "Please allow some time for the funds to reflect in your crypto wallet.\n\n"
                + "If you did not initiate this withdrawal, please contact our support team immediately.\n\n"
                + "Thank you,\n"
                + "Team XinPay";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@xinpay.co.in");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    
    public void sendInrWithdrawApprovedEmail(String toEmail, String fullName, double amount) {
        String subject = "XinPay - INR Withdrawal processed ✅";
        String body = "Dear " + fullName + ",\n\n"
                + "✅ Your INR withdrawal of ₹" + amount + " has been approved and processed.\n\n"
                + "Please allow some time for the funds to reach your bank account.\n\n"
                + "If you did not initiate this withdrawal, please contact our support team immediately.\n\n"
                + "Thank you,\n"
                + "Team XinPay";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@xinpay.co.in");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    
    public void sendInrDepositEmailAndNotify(User user, double amount) {
        // Step 1: Send email using existing method
        sendInrDepositApprovedEmail(user.getEmail(), user.getFullName(), amount);

        // Step 2: Send FCM push notification if token exists
        if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
            try {
                notificationService.sendInrDepositApproved(user.getFcmToken(), amount);
            } catch (FirebaseMessagingException e) {
                System.err.println("❌ FCM push failed: " + e.getMessage());
            }
        }
    }






}
