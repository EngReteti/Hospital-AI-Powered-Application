package com.amason.hospitalinventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// @Entity tells Spring Boot: "this class represents a database table"
// @Table lets us name that table explicitly - here, "users"
@Entity
@Table(name = "users")
public class User {

    // @Id marks this field as the table's primary key (unique row identifier)
    // @GeneratedValue means the database auto-generates this number for us
    // (1, 2, 3...) — we never set it manually
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = false means this column is REQUIRED - the database will
    // reject any attempt to save a User without a name
    @Column(nullable = false)
    private String name;

    // unique = true means no two users can share the same email -
    // the database itself enforces this, not just our Java code
    @Column(nullable = false, unique = true)
    private String email;

    // We store a HASHED password, never the real password in plain text.
    // The actual hashing happens later, in the Auth phase - this field
    // just holds whatever hashed value we give it
    @Column(nullable = false)
    private String passwordHash;

    // @Enumerated(STRING) means the role is stored as readable text
    // ("ADMIN", not "0") in the database - much easier to read directly
    // in PostgreSQL if we ever need to check it manually
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    // This runs automatically right before the row is first saved -
    // it stamps the current date/time, so we never forget to set it manually
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and setters below ---
    // Hibernate and Spring need these to read/write each field.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
