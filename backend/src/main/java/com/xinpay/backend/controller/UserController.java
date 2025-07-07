// ✅ UserController.java
package com.xinpay.backend.controller;

import com.xinpay.backend.model.User;
import com.xinpay.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        return userOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<User> updateProfileImage(@PathVariable Long id, @RequestParam String imageUrl) {
        try {
            User updatedUser = userService.updateProfileImage(id, imageUrl);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/fcm-token")
    public ResponseEntity<User> updateFcmToken(@PathVariable Long id, @RequestParam String token) {
        try {
            User updatedUser = userService.updateFcmToken(id, token);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
