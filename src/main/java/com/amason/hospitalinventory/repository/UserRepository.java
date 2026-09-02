package com.amason.hospitalinventory.repository;

import com.amason.hospitalinventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<User, Long> means: "this repository manages User entities, 
// and each User's ID is a Long." Just by extending this interface, we 
// instantly get free methods like save(), findById(), findAll(), delete() 
// — no code needed, Spring Boot generates the implementation automatically
public interface UserRepository extends JpaRepository<User, Long> {

    // This is a CUSTOM query method. Spring Boot reads the method NAME 
    // itself and generates the correct SQL from it - "findByEmail" 
    // automatically becomes "SELECT * FROM users WHERE email = ?"
    // We'll use this during login, to look up a user by their email
    Optional<User> findByEmail(String email);
}
