package com.xinpay.backend.config;

import com.xinpay.backend.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ❌ Disable CSRF for APIs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",                  // ✅ Public auth
                    "/ping", "/error", "/",      // ✅ Health, home
                    "/test/**",                  // ✅ Test
                    "/uploads/**",               // ✅ Uploaded files
                    "/api/upload",               // ✅ Upload
                    "/api/deposit/status/**",    // ✅ Deposit status
                    "/api/inr-deposits/**",      // ✅ INR deposit
                    "/api/usdt-deposits/**",     // ✅ USDT deposit
                    "/api/inr-withdraw/**",      // ✅ ✅ Allow INR withdraw endpoints
                    "/api/accounts/**",
                    "/api/usdt-withdraw/**",
                		"/api/bank-details/**",
                		"/api/notifications/**",
                		"/api/user/**",
                    "/api/commissions/**",
                    "/api/wallet/**",
                    "/api/balance/**",         // ✅ Balance
                    "/api/test/**"
                    
                ).permitAll()
                .anyRequest().authenticated()   // 🔐 Require auth for other routes
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
