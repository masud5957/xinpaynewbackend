package com.xinpay.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.InputStream;

public class FirebaseInitializer {

    private static boolean initialized = false;

    public static synchronized void init() {
        if (initialized) return;

        try (InputStream serviceAccount = FirebaseInitializer.class.getClassLoader()
                .getResourceAsStream("firebase/firebase-service-account.json")) {

            if (serviceAccount == null) {
                throw new IllegalStateException("Firebase credentials file not found.");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            initialized = true;

            System.out.println("✅ Firebase initialized manually");

        } catch (Exception e) {
            System.err.println("❌ Firebase manual init failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
