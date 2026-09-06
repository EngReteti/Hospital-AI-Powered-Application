package com.amason.hospitalinventory.controller;

import com.amason.hospitalinventory.model.User;
import com.amason.hospitalinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // NOTE: this endpoint currently saves passwordHash exactly as sent -
    // real password HASHING (turning "mypassword123" into a secure, 
    // irreversible scrambled value) belongs in Phase 9 (Auth), not here.
    // This is a temporary gap, same honesty as our open SecurityConfig -
    // fine for now, must be fixed before this is ever real/deployed
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
