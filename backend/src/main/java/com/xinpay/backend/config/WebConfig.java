package com.xinpay.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * ✅ Allow Cross-Origin requests (for Postman, frontend apps, mobile, etc.)
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 🔓 Allow all origins (change to your frontend URL in production)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

    /**
     * ✅ Serve files from local uploads directory mapped to /uploads/**
     * For example: http://localhost:8080/uploads/<filename>.jpg
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Use user's home directory + /xinpay-uploads/
        String uploadDir = Paths.get(System.getProperty("user.home"), "xinpay-uploads").toString();

        // Serve files from file:/<path>/ mapped to /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
