package com.xinpay.backend.service;

import com.xinpay.backend.model.User;
import com.xinpay.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateProfileImage(Long userId, String imageUrl) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setProfileImageUrl(imageUrl);
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }

    public User updateFcmToken(Long userId, String fcmToken) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFcmToken(fcmToken);
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found");
    }
}
