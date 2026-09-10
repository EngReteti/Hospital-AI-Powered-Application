package com.amason.hospitalinventory.controller;

import com.amason.hospitalinventory.dto.LoginRequest;
import com.amason.hospitalinventory.model.User;
import com.amason.hospitalinventory.repository.UserRepository;
import com.amason.hospitalinventory.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        // Step 1: look up the user by email. If no such email exists, 
        // we throw the SAME generic error we'll use for a wrong 
        // password too 
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Step 2: check the password. passwordEncoder.matches() hashes 
        // whatever was typed and compares it to the stored hash - we 
        // NEVER decode the stored hash back into readable text, because 
        // that's not actually possible with BCrypt, by design
        boolean passwordCorrect = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!passwordCorrect) {
            throw new RuntimeException("Invalid email or password");
        }

        // Step 3: credentials are correct - generate a real token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().toString());

        // Step 4: return it in a small, clean response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole().toString());

        return response;
    }
}
