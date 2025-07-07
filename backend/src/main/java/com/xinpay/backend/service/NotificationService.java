package com.xinpay.backend.service;

import com.google.firebase.messaging.*;
import com.xinpay.backend.config.FirebaseInitializer;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // 🔔 Common method to send push notification
    private void sendNotification(String fcmToken, String title, String body) throws FirebaseMessagingException {
    	
    	
    	FirebaseInitializer.init(); // ✅ Make sure Firebase is initialized
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("✅ Notification sent: " + response);
    }

    // 📩 INR Deposit Approved
    public void sendInrDepositApproved(String fcmToken, double amount) throws FirebaseMessagingException {
        String title = "Deposit Successful";
        String body = "Your INR deposit of ₹" + amount + " has been successfully approved.";
        sendNotification(fcmToken, title, body);
    }

    // 📩 USDT Deposit Approved
    public void sendUsdtDepositApproved(String fcmToken, double amount) throws FirebaseMessagingException {
        String title = "Deposit Successful";
        String body = "Your USDT deposit of " + amount + " USDT is now available in your wallet.";
        sendNotification(fcmToken, title, body);
    }

    // 📤 INR Withdraw Approved
    public void sendInrWithdrawApproved(String fcmToken, double amount) throws FirebaseMessagingException {
        String title = "Withdrawal Processed";
        String body = "Your INR withdrawal request of ₹" + amount + " has been processed successfully.";
        sendNotification(fcmToken, title, body);
    }

    // 📤 USDT Withdraw Approved
    public void sendUsdtWithdrawApproved(String fcmToken, double amount) throws FirebaseMessagingException {
        String title = "Withdrawal Processed";
        String body = "Your USDT withdrawal of " + amount + " USDT has been approved and sent.";
        sendNotification(fcmToken, title, body);
    }
}
