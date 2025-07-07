package com.xinpay.backend.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.xinpay.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class NotificationTestController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/fcm")
    public ResponseEntity<String> sendTestNotification(@RequestParam String token) {
        try {
            notificationService.sendInrDepositApproved(token, 999.99);
            return ResponseEntity.ok("✅ Test notification sent to token.");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Failed to send notification: " + e.getMessage());
        }
    }
}
