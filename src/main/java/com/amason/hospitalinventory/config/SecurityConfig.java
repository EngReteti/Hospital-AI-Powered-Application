package com.amason.hospitalinventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// @Configuration tells Spring: "this class sets up how a part of the
// app behaves" - here, specifically Security's rules
@Configuration
public class SecurityConfig {
// @Bean means: "Spring, create ONE of these and hand it out anywhere 
    // it's needed in the app - don't make a new one every time"
    // BCryptPasswordEncoder is the actual hashing tool - it turns 
    // "temporary123" into something like 
    // "$2a$10$N9qo8uLOickgx2ZMRZoMy..." - irreversible, one-way
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // This method replaces Spring Security's default "block everything" 
    // behavior with our own temporary rule: allow all requests through, 
    // no login required - FOR NOW, while we build and test features.
    // In Phase 9, we will replace this with real JWT-based rules:
    // e.g. "only ADMIN can create products", "only logged-in users can 
    // record stock movements"
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disables a browser-focused protection (CSRF) that doesn't 
            // apply the same way to a REST API like ours
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // "anyRequest().permitAll()" means: let every single 
                // request through without checking login - TEMPORARY
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
