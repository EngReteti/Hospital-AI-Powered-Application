package com.amason.hospitalinventory.dto;

// A DTO (Data Transfer Object) - a simple class whose only job is 
// carrying data between the outside world and our code, with no 
// business logic in it at all. This is different from an @Entity - 
// this class never becomes a database table, it just shapes the 
// incoming JSON from a login request: { "email": "...", "password": "..." }
public class LoginRequest {

    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
