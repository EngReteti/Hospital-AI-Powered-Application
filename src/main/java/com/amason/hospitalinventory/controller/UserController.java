package com.amason.hospitalinventory.controller;

import com.amason.hospitalinventory.model.User;
import com.amason.hospitalinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Spring automatically gives us the exact PasswordEncoder bean
    // we just created in SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // Hash whatever plain-text password came in the request,
        // BEFORE saving - this is the only place a real password
        // ever briefly exists in memory, and it's never written to
        // disk in that form
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        return userRepository.save(user);
    }
}
